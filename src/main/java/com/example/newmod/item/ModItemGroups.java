package com.example.newmod.item;

import com.example.newmod.NewMod;
import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;

/**
 * 创造模式物品栏里的 NewMod 专属页。
 *
 * <p>页签注册在 {@code newmod:main} 下，图标用模组史莱姆刷怪蛋；
 * 之后新增的模组物品只要在 {@code displayItems} 里加一行即可。
 */
public final class ModItemGroups {

	/** 页签键，同时作为注册 ID：{@code newmod:main}。 */
	public static final ResourceKey<CreativeModeTab> NEWMOD_TAB_KEY =
			ResourceKey.create(Registries.CREATIVE_MODE_TAB, NewMod.id("main"));

	private ModItemGroups() {
	}

	public static void register() {
		Registry.register(
				BuiltInRegistries.CREATIVE_MODE_TAB,
				NEWMOD_TAB_KEY,
				FabricItemGroup.builder()
						.title(Component.translatable("itemGroup.newmod.main"))
						.icon(() -> new ItemStack(ModItems.MOD_SLIME_SPAWN_EGG))
						.displayItems((parameters, output) -> {
							// 模组物品统一放这里
							output.accept(ModItems.MOD_SLIME_SPAWN_EGG);
						})
						.build()
		);
	}
}
