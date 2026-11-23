package com.example.newmod.mixin;

import com.example.newmod.entity.ModSlime;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 用模组实现整体替换原版史莱姆。
 *
 * <p>所有实体创建（自然生成、刷怪蛋、刷怪笼、{@code /summon}、区块读档、客户端同步）
 * 最终都会走到 {@link EntityType#create(Level)}，所以只要在这里把
 * {@code minecraft:slime} 的创建结果换成 {@link ModSlime}，原版史莱姆就变成了
 * 带驯服/护主逻辑的版本，实体 ID、存档、刷怪蛋都与原版保持一致。
 */
@Mixin(EntityType.class)
public abstract class EntityTypeMixin {

	@Inject(method = "create(Lnet/minecraft/world/level/Level;)Lnet/minecraft/world/entity/Entity;",
			at = @At("HEAD"), cancellable = true)
	private void newmod$replaceVanillaSlime(Level level, CallbackInfoReturnable<Entity> cir) {
		if ((Object) this == EntityType.SLIME) {
			cir.setReturnValue(new ModSlime(EntityType.SLIME, level));
		}
	}
}
