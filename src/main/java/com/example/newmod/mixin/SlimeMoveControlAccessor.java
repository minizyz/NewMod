package com.example.newmod.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

/**
 * 访问原版 {@code Slime.SlimeMoveControl} 的转向/速度设置。
 *
 * <p>这个内部类是包私有的，模组代码无法直接引用，所以用 Mixin 暴露两个调用入口，
 * 让已驯服的史莱姆可以主动朝主人方向跳。
 */
@Mixin(targets = "net.minecraft.world.entity.monster.Slime$SlimeMoveControl")
public interface SlimeMoveControlAccessor {

	@Invoker("setDirection")
	void newmod$setDirection(float yRot, boolean aggressive);

	@Invoker("setWantedMovement")
	void newmod$setWantedMovement(double speed);
}
