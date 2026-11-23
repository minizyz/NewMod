package com.example.newmod.client;

import com.example.newmod.NewMod;
import com.example.newmod.entity.ModSlime;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.SlimeModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

/**
 * 自定义史莱姆渲染器。
 *
 * <p>模型层与贴图都注册在 {@code newmod} 命名空间下，不复用原版的
 * {@code minecraft:mod_slime} 模型层或 {@code minecraft:textures/entity/slime/slime.png}，
 * 因此这个生物与原版史莱姆完全独立。
 */
@Environment(EnvType.CLIENT)
public class ModSlimeRenderer extends MobRenderer<ModSlime, SlimeModel<ModSlime>> {

	/** 内层身体（含眼睛、嘴）模型层。 */
	public static final ModelLayerLocation INNER_LAYER =
			new ModelLayerLocation(NewMod.id("mod_slime"), "main");
	/** 外层果冻模型层。 */
	public static final ModelLayerLocation OUTER_LAYER =
			new ModelLayerLocation(NewMod.id("mod_slime"), "outer");

	private static final ResourceLocation TEXTURE =
			NewMod.id("textures/entity/mod_slime.png");

	public ModSlimeRenderer(EntityRendererProvider.Context context) {
		super(context, new SlimeModel<>(context.bakeLayer(INNER_LAYER)), 0.25F);
		this.addLayer(new ModSlimeOuterLayer<>(this, context.getModelSet()));
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
		return TEXTURE;
	}
}
