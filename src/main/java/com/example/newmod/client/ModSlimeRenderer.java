package com.example.newmod.client;

import com.example.newmod.entity.ModSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.SlimeOuterLayer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 史莱姆渲染器：复用原版史莱姆的模型（内层 + 外层果冻层）与原版纹理，
 * 只把泛型换成自定义的 {@link ModSlime}，因此外观与缩放逻辑和原版一致。
 */
@Environment(EnvType.CLIENT)
public class ModSlimeRenderer extends MobRenderer<ModSlime, SlimeModel<ModSlime>> {
	private static final ResourceLocation SLIME_LOCATION =
			ResourceLocation.withDefaultNamespace("textures/entity/slime/slime.png");

	public ModSlimeRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeModel<>(context.bakeLayer(ModelLayers.SLIME)), 0.25F);
		this.addLayer(new SlimeOuterLayer<>(this, context.getModelSet()));
	}

	@Override
	public void render(ModSlime slime, float yaw, float partialTick, PoseStack poseStack,
					   MultiBufferSource bufferSource, int packedLight) {
		this.shadowRadius = 0.25F * slime.getSize();
		super.render(slime, yaw, partialTick, poseStack, bufferSource, packedLight);
	}

	@Override
	protected void scale(ModSlime slime, PoseStack poseStack, float partialTick) {
		float size = slime.getSize();
		float squish = Mth.lerp(partialTick, slime.oSquish, slime.squish) / (size * 0.5F + 1.0F);
		float inverse = 1.0F / (squish + 1.0F);
		poseStack.scale(0.999F, 0.999F, 0.999F);
		poseStack.translate(0.0F, 0.001F, 0.0F);
		poseStack.scale(inverse * size, 1.0F / inverse * size, inverse * size);
	}

	@Override
	public ResourceLocation getTextureLocation(ModSlime slime) {
		return SLIME_LOCATION;
	}
}
