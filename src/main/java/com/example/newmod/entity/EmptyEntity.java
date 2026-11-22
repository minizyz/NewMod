package com.example.newmod.entity;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.level.Level;

/**
 * 一个暂时为空的生物：
 * - 不赋予任何 AI 行为（registerGoals 留空，未注册任何目标）；
 * - 不绑定材质（渲染器中仅返回占位纹理路径，纹理文件后续补充）。
 */
public class EmptyEntity extends Mob {

	public EmptyEntity(EntityType<? extends Mob> entityType, Level level) {
		super(entityType, level);
	}

	/**
	 * 默认属性，后续可按需求调整血量、移动速度等。
	 */
	public static AttributeSupplier.Builder createAttributes() {
		return Mob.createMobAttributes();
	}

	@Override
	protected void registerGoals() {
		// 暂不添加任何 AI 目标
	}
}
