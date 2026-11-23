package com.example.newmod.item;

import com.example.newmod.NewMod;
import com.example.newmod.entity.ModEntities;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SpawnEggItem;

public final class ModItems {

	/** 自定义史莱姆刷怪蛋，颜色取自史莱姆的深绿/浅绿。 */
	public static final Item MOD_SLIME_SPAWN_EGG = Registry.register(
			BuiltInRegistries.ITEM,
			NewMod.id("mod_slime_spawn_egg"),
			new SpawnEggItem(ModEntities.MOD_SLIME, 0x2C5E1F, 0x74C466, new Item.Properties())
	);

	private static final ResourceKey<CreativeModeTab> SPAWN_EGGS_TAB =
			ResourceKey.create(Registries.CREATIVE_MODE_TAB, ResourceLocation.withDefaultNamespace("spawn_eggs"));

	private ModItems() {
	}

	public static void register() {
		// 放进创造模式“刷怪蛋”分类
		ItemGroupEvents.modifyEntriesEvent(SPAWN_EGGS_TAB)
				.register(entries -> entries.accept(MOD_SLIME_SPAWN_EGG));
	}
}
