package com.example.newmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.monster.Slime;
import net.minecraft.world.level.Level;

/**
 * 自定义史莱姆生物。
 *
 * <p>这里不在本地重写 1.21.1 的 Slime 代码，而是直接继承原版 {@link Slime}，
 * 跳跃移动控制、SlimeMoveControl、随机跳、攻击玩家、死亡分裂、挤压音效与粒子等
 * 行为全部走父类实现，效果与原版史莱姆一致。
 *
 * <p>后续要加功能时，在子类里覆写对应方法（如 tick、dealDamage、remove、finalizeSpawn、
 * getParticleType 等）并调用 super 保留原版逻辑即可。
 */
public class ModSlime extends Slime {

	public ModSlime(EntityType<? extends Slime> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * 默认属性：原版史莱姆没有额外属性，血量/速度/攻击力会在 setSize 时按体积重设。
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes();
	}
}
