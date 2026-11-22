package com.example.newmod.client;

import com.example.newmod.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;

public class NewModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// 注册空生物的渲染器（占位，暂无材质）
		EntityRendererRegistry.register(ModEntities.EMPTY_ENTITY, EmptyEntityRenderer::new);
	}
}
