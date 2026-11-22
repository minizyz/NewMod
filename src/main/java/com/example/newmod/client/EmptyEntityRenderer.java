package com.example.newmod.client;

import com.example.newmod.NewMod;
import com.example.newmod.entity.EmptyEntity;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

/**
 * 空生物的占位渲染器。
 * 目前只返回预留的纹理路径，尚未提供实际材质文件；
 * 后续补充模型与贴图后再替换本渲染器。
 */
public class EmptyEntityRenderer extends EntityRenderer<EmptyEntity> {

	public EmptyEntityRenderer(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public ResourceLocation getTextureLocation(EmptyEntity entity) {
		// TODO: 占位材质，待补充 assets/newmod/textures/entity/empty_entity.png
		return NewMod.id("textures/entity/empty_entity.png");
	}
}
