package com.mochi_753.crazy_steve.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

@OnlyIn(Dist.CLIENT)
@SuppressWarnings("deprecation")
public class CustomModelRenderer {
    public static void renderModel(PoseStack poseStack, VertexConsumer consumer, BakedModel model, int light, int overlay) {
        RandomSource randomSource = RandomSource.create();

        for (Direction direction : Direction.values()) {
            randomSource.setSeed(42L);
            List<BakedQuad> quads = model.getQuads(null, direction, randomSource);
            renderQuads(poseStack, consumer, quads, light, overlay);
        }

        randomSource.setSeed(42L);
        List<BakedQuad> quads = model.getQuads(null, null, randomSource);
        renderQuads(poseStack, consumer, quads, light, overlay);
    }

    public static void renderModel(PoseStack poseStack, VertexConsumer consumer, BakedModel model, int light) {
        renderModel(poseStack, consumer, model, light, OverlayTexture.NO_OVERLAY);
    }

    public static void renderModel(PoseStack poseStack, VertexConsumer consumer, BakedModel model) {
        renderModel(poseStack, consumer, model, LightTexture.FULL_BRIGHT, OverlayTexture.NO_OVERLAY);
    }

    private static void renderQuads(PoseStack poseStack, VertexConsumer consumer, List<BakedQuad> quads, int light, int overlay) {
        PoseStack.Pose pose = poseStack.last();

        for (BakedQuad quad : quads) {
            consumer.putBulkData(pose, quad, 1.0F, 1.0F, 1.0F, light, overlay);
        }
    }
}
