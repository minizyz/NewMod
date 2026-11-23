package com.example.newmod.client;

import com.example.newmod.entity.ModEntities;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.EntityModelLayerRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.model.SlimeModel;

public class NewModClient implements ClientModInitializer {

	@Override
	public void onInitializeClient() {
		// 注册 newmod 命名空间下的史莱姆模型层（内层身体 + 外层果冻）
		EntityModelLayerRegistry.registerModelLayer(ModSlimeRenderer.INNER_LAYER, SlimeModel::createInnerBodyLayer);
		EntityModelLayerRegistry.registerModelLayer(ModSlimeRenderer.OUTER_LAYER, SlimeModel::createOuterBodyLayer);

		// 注册空生物的渲染器（占位，暂无材质）
		EntityRendererRegistry.register(ModEntities.EMPTY_ENTITY, EmptyEntityRenderer::new);

		// 自定义史莱姆：独立模型层与独立贴图
		EntityRendererRegistry.register(ModEntities.MOD_SLIME, ModSlimeRenderer::new);
	}
}
