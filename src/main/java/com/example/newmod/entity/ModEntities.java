package com.example.newmod.entity;

import com.example.newmod.NewMod;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;

public final class ModEntities {

	public static final EntityType<EmptyEntity> EMPTY_ENTITY = Registry.register(
			BuiltInRegistries.ENTITY_TYPE,
			NewMod.id("empty_entity"),
			EntityType.Builder.of(EmptyEntity::new, MobCategory.MISC)
					.sized(0.6F, 1.8F)
					.clientTrackingRange(8)
					.build("empty_entity")
	);

	private ModEntities() {
	}

	public static void register() {
		// 触发实体类型类加载，并注册默认属性
		FabricDefaultAttributeRegistry.register(EMPTY_ENTITY, EmptyEntity.createAttributes());
	}
}
