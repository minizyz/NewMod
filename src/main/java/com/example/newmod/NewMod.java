package com.example.newmod;

import com.example.newmod.entity.ModEntities;
import net.fabricmc.api.ModInitializer;
import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NewMod implements ModInitializer {
	public static final String MOD_ID = "newmod";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		// 注册实体类型与默认属性
		ModEntities.register();
		LOGGER.info("NewMod initialized.");
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.fromNamespaceAndPath(MOD_ID, path);
	}
}
