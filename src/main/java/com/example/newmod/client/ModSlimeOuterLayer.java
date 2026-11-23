package com.example.newmod.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.RenderLayerParent;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.world.entity.LivingEntity;

/**
 * 史莱姆外层果冻层，模型层取自 {@link ModSlimeRenderer#OUTER_LAYER}，
 * 因此外层也完全放在 newmod 命名空间下。
 */
@Environment(EnvType.CLIENT)
public class ModSlimeOuterLayer<T extends LivingEntity> extends RenderLayer<T, SlimeModel<T>> {
	private final EntityModel<T> model;

	public ModSlimeOuterLayer(RenderLayerParent<T, SlimeModel<T>> parent, EntityModelSet modelSet) {
		super(parent);
		this.model = new SlimeModel<>(modelSet.bakeLayer(ModSlimeRenderer.OUTER_LAYER));
	}

	@Override
	public void render(PoseStack poseStack, MultiBufferSource bufferSource, int packedLight,
					   T livingEntity, float limbSwing, float limbSwingAmount, float partialTick,
					   float ageInTicks, float netHeadYaw, float headPitch) {
		Minecraft minecraft = Minecraft.getInstance();
		boolean glowing = minecraft.shouldEntityAppearGlowing(livingEntity) && livingEntity.isInvisible();
		if (!livingEntity.isInvisible() || glowing) {
			VertexConsumer vertexConsumer = glowing
					? bufferSource.getBuffer(RenderType.outline(this.getTextureLocation(livingEntity)))
					: bufferSource.getBuffer(RenderType.entityTranslucent(this.getTextureLocation(livingEntity)));

			this.getParentModel().copyPropertiesTo(this.model);
			this.model.prepareMobModel(livingEntity, limbSwing, limbSwingAmount, partialTick);
			this.model.setupAnim(livingEntity, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch);
			this.model.renderToBuffer(poseStack, vertexConsumer, packedLight,
					LivingEntityRenderer.getOverlayCoords(livingEntity, 0.0F));
		}
	}
}
