package com.example.newmod.entity;

import java.util.Optional;
import java.util.UUID;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;

/**
 * 自定义史莱姆生物（独立于原版 slime 的实体类型、模型层与贴图）。
 *
 * <p>行为主体直接沿用原版 {@link Slime}：跳跃移动、随机跳、挤压音效与粒子、
 * 死亡分裂、对非驯服目标的接触伤害等全部由父类实现。
 *
 * <p>在此基础上新增粘液球驯服与主人保护逻辑：
 * <ul>
 *   <li>手持粘液球右键：每次 30% 成功率，第 5 次尝试必定成功</li>
 *   <li>驯服后不再主动把玩家设为目标</li>
 *   <li>驯服者（含其射出的箭等投射物）无法伤害它</li>
 *   <li>驯服者被攻击时，它会主动把攻击者设为目标并反击</li>
 * </ul>
 */
public class ModSlime extends Slime {

	/** 单次驯服成功率。 */
	public static final float TAME_CHANCE = 0.3F;
	/** 第几次尝试必定成功。 */
	public static final int GUARANTEED_TAME_ATTEMPT = 5;

	private static final EntityDataAccessor<Optional<UUID>> DATA_OWNER =
			SynchedEntityData.defineId(ModSlime.class, EntityDataSerializers.OPTIONAL_UUID);

	/** 已尝试驯服的次数（服务端字段，随 NBT 保存）。 */
	private int tameAttempts;
	/** 主人最后一次受伤的时间戳，用于只对新的攻击做出反应。 */
	private int lastOwnerHurtTimestamp;
	/** 因主人被攻击而锁定的攻击者；允许对这名玩家反击。 */
	private Player revengePlayer;
	/** 设置目标时临时绕过“驯服后不主动攻击玩家”的限制。 */
	private boolean bypassTargetFilter;

	public ModSlime(EntityType<? extends Slime> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * 默认属性必须和原版史莱姆一致：{@link Monster#createMonsterAttributes()} 比
	 * {@code Mob.createMobAttributes()} 多一条 ATTACK_DAMAGE。史莱姆的 {@code setSize}
	 * 会直接读写这个属性，缺了它会在生成时抛空指针，导致刷怪蛋/召唤失败。
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Monster.createMonsterAttributes();
	}

	@Override
	protected void defineSynchedData(SynchedEntityData.Builder builder) {
		super.defineSynchedData(builder);
		builder.define(DATA_OWNER, Optional.empty());
	}

	// ------------------------------------------------------------------
	// 驯服状态
	// ------------------------------------------------------------------

	public boolean isTamed() {
		return this.entityData.get(DATA_OWNER).isPresent();
	}

	public Optional<UUID> getOwnerUUID() {
		return this.entityData.get(DATA_OWNER);
	}

	/** 取主人；客户端没有服务器玩家列表，返回 null。 */
	public Player getOwner() {
		Optional<UUID> uuid = this.getOwnerUUID();
		if (uuid.isEmpty() || this.level().getServer() == null) {
			return null;
		}
		return this.level().getServer().getPlayerList().getPlayer(uuid.get());
	}

	public boolean isOwner(Entity entity) {
		if (!(entity instanceof Player player) || !this.isTamed()) {
			return false;
		}
		return this.getOwnerUUID().map(uuid -> uuid.equals(player.getUUID())).orElse(false);
	}

	private void setTamedBy(Player player) {
		this.entityData.set(DATA_OWNER, Optional.of(player.getUUID()));
		this.setPersistenceRequired();
		// 忽略驯服前主人受过的旧攻击，只响应之后新发生的伤害
		this.lastOwnerHurtTimestamp = player.getLastHurtByMobTimestamp();
		// 驯服瞬间如果正追着某个玩家，立刻放弃该目标
		if (this.getTarget() instanceof Player) {
			this.setTarget(null);
		}
	}

	// ------------------------------------------------------------------
	// 用粘液球驯服
	// ------------------------------------------------------------------

	@Override
	protected InteractionResult mobInteract(Player player, InteractionHand hand) {
		ItemStack stack = player.getItemInHand(hand);
		if (!this.isTamed() && stack.is(Items.SLIME_BALL)) {
			if (!this.level().isClientSide) {
				this.tameAttempts++;
				boolean success = this.tameAttempts >= GUARANTEED_TAME_ATTEMPT
						|| this.getRandom().nextFloat() < TAME_CHANCE;

				if (success) {
					this.setTamedBy(player);
				}
				this.playTameEffects(success);

				if (!player.getAbilities().instabuild) {
					stack.shrink(1);
				}
			}
			return InteractionResult.sidedSuccess(this.level().isClientSide);
		}
		return super.mobInteract(player, hand);
	}

	private void playTameEffects(boolean success) {
		if (!(this.level() instanceof ServerLevel serverLevel)) {
			return;
		}
		if (success) {
			serverLevel.sendParticles(ParticleTypes.HEART,
					this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
					7, 0.3D, 0.3D, 0.3D, 0.0D);
			serverLevel.playSound(null, this.blockPosition(),
					SoundEvents.SLIME_SQUISH, SoundSource.NEUTRAL, 1.0F, 1.0F);
		} else {
			serverLevel.sendParticles(ParticleTypes.SMOKE,
					this.getX(), this.getY() + this.getBbHeight() * 0.5D, this.getZ(),
					4, 0.25D, 0.25D, 0.25D, 0.0D);
			serverLevel.playSound(null, this.blockPosition(),
					SoundEvents.SLIME_SQUISH_SMALL, SoundSource.NEUTRAL, 0.8F, 1.2F);
		}
	}

	// ------------------------------------------------------------------
	// 目标与伤害规则
	// ------------------------------------------------------------------

	@Override
	public void setTarget(LivingEntity target) {
		if (!this.bypassTargetFilter && this.isTamed() && target instanceof Player player && !this.isOwner(player)) {
			// 已驯服的史莱姆不会主动把其他玩家设为目标
			super.setTarget(null);
			return;
		}
		super.setTarget(target);
	}

	@Override
	public boolean canAttack(LivingEntity target) {
		if (!super.canAttack(target)) {
			return false;
		}
		if (this.isTamed() && target instanceof Player player) {
			// 驯服后：主人永远免疫，其他玩家只有在“替主人反击”时才允许攻击
			return player == this.revengePlayer;
		}
		return true;
	}

	@Override
	public void playerTouch(Player player) {
		if (this.isOwner(player)) {
			// 碰到主人不造成伤害
			return;
		}
		super.playerTouch(player);
	}

	@Override
	public boolean hurt(DamageSource source, float amount) {
		if (this.isTamed() && (this.isOwner(source.getEntity()) || this.isOwner(source.getDirectEntity()))) {
			// 主人近战、弓箭、投掷物等造成的伤害一律无效
			return false;
		}
		return super.hurt(source, amount);
	}

	@Override
	public void tick() {
		super.tick();
		if (this.level().isClientSide || !this.isTamed()) {
			return;
		}

		Player owner = this.getOwner();
		if (owner == null || !owner.isAlive()) {
			return;
		}

		LivingEntity attacker = owner.getLastHurtByMob();
		if (attacker == null || !attacker.isAlive() || attacker == this || this.isOwner(attacker)) {
			return;
		}
		// 只对新的受伤事件做出反应
		if (owner.getLastHurtByMobTimestamp() == this.lastOwnerHurtTimestamp) {
			return;
		}
		this.lastOwnerHurtTimestamp = owner.getLastHurtByMobTimestamp();

		if (this.distanceToSqr(attacker) > 32.0D * 32.0D) {
			return;
		}

		if (attacker instanceof Player player) {
			this.revengePlayer = player;
		}
		this.bypassTargetFilter = true;
		this.setTarget(attacker);
		this.bypassTargetFilter = false;
	}

	@Override
	protected boolean shouldDespawnInPeaceful() {
		// 已驯服的史莱姆不会在和平难度下被清除
		return !this.isTamed();
	}

	// ------------------------------------------------------------------
	// 存档
	// ------------------------------------------------------------------

	@Override
	public void addAdditionalSaveData(CompoundTag compoundTag) {
		super.addAdditionalSaveData(compoundTag);
		compoundTag.putInt("NewModTameAttempts", this.tameAttempts);
		this.getOwnerUUID().ifPresent(uuid -> compoundTag.putUUID("NewModOwner", uuid));
	}

	@Override
	public void readAdditionalSaveData(CompoundTag compoundTag) {
		super.readAdditionalSaveData(compoundTag);
		this.tameAttempts = compoundTag.getInt("NewModTameAttempts");
		if (compoundTag.contains("NewModOwner")) {
			this.entityData.set(DATA_OWNER, Optional.of(compoundTag.getUUID("NewModOwner")));
		}
	}
}
