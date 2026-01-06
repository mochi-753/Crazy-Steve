package com.mochi_753.crazy_steve.client.renderer;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class RenderUtils {
    public static void with(PoseStack poseStack, Runnable runnable) {
        poseStack.pushPose();
        runnable.run();
        poseStack.popPose();
    }
}
