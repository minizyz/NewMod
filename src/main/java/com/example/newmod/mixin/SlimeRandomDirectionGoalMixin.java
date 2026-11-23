package com.example.newmod.mixin;

import com.example.newmod.entity.ModSlime;
import net.minecraft.world.entity.monster.Slime;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

/**
 * 已驯服的史莱姆不再随机选方向（原版那个 goal 每 tick 都会把朝向改成随机角度，
 * 会覆盖跟随主人的转向），改由 {@link ModSlime} 自己驱动朝主人跳。
 */
@Mixin(targets = "net.minecraft.world.entity.monster.Slime$SlimeRandomDirectionGoal")
public abstract class SlimeRandomDirectionGoalMixin {

	@Shadow
	@Final
	private Slime slime;

	@Inject(method = "canUse", at = @At("HEAD"), cancellable = true)
	private void newmod$disableRandomWanderWhenTamed(CallbackInfoReturnable<Boolean> cir) {
		if (this.slime instanceof ModSlime modSlime && modSlime.isTamed()) {
			cir.setReturnValue(false);
		}
	}
}
