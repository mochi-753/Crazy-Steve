package com.mochi_753.crazy_steve.client.entity;

import com.mochi_753.crazy_steve.CrazySteve;
import com.mochi_753.crazy_steve.client.renderer.CustomModelRenderer;
import com.mochi_753.crazy_steve.client.renderer.RenderUtils;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Camera;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@OnlyIn(Dist.CLIENT)
public class ContinuePipeRenderer {
    @SuppressWarnings("removal")
    private static final ResourceLocation MODEL_LOCATION = new ResourceLocation(CrazySteve.MOD_ID, "custom/continue_pipe");
    private static final int MAX_LIFETIME_TICKS = 100;
    private static final List<ContinuePipeRenderer> RENDERERS = new ArrayList<>();
    private final Vec3 position;
    private final ResourceKey<Level> dimension;
    private int tickCount;

    public ContinuePipeRenderer(Vec3 position, ResourceKey<Level> dimension) {
        this.position = position;
        this.dimension = dimension;
        this.tickCount = 0;
        RENDERERS.add(this);
    }

    public static void tickAll() {
        Iterator<ContinuePipeRenderer> iterator = RENDERERS.iterator();
        while (iterator.hasNext()) {
            ContinuePipeRenderer renderer = iterator.next();
            renderer.tick();
            if (renderer.isExpired()) {
                iterator.remove();
            }
        }
    }

    public static void renderAll(ClientLevel level, PoseStack poseStack, VertexConsumer consumer, Camera camera, BakedModel model) {
        for (ContinuePipeRenderer renderer : RENDERERS) {
            renderer.render(level, poseStack, consumer, camera, model);
        }
    }

    public static void clearAll() {
        RENDERERS.clear();
    }

    private void tick() {
        tickCount++;
    }

    private boolean isExpired() {
        return tickCount > MAX_LIFETIME_TICKS;
    }

    private void render(ClientLevel level, PoseStack poseStack, VertexConsumer consumer, Camera camera, BakedModel model) {
        if (level == null || !level.dimension().equals(dimension)) return;

        RenderUtils.with(poseStack, () -> {
            Vec3 relativePos = position.subtract(camera.getPosition());
            poseStack.translate(relativePos.x(), relativePos.y(), relativePos.z());
            CustomModelRenderer.renderModel(poseStack, consumer, model);
        });
    }

    @Mod.EventBusSubscriber(modid = CrazySteve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public static class EventHandler {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                ContinuePipeRenderer.tickAll();
            }
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            if (event.getLevel().isClientSide()) {
                ContinuePipeRenderer.clearAll();
            }
        }

        @SubscribeEvent
        public static void onRenderLevelStage(RenderLevelStageEvent event) {
            if (event.getStage() != RenderLevelStageEvent.Stage.AFTER_ENTITIES) return;

            Minecraft mc = Minecraft.getInstance();

            ClientLevel level = mc.level;
            PoseStack poseStack = event.getPoseStack();
            VertexConsumer consumer = mc.renderBuffers().bufferSource().getBuffer(RenderType.solid());
            Camera camera = event.getCamera();
            BakedModel model = mc.getModelManager().getModel(MODEL_LOCATION);

            ContinuePipeRenderer.renderAll(level, poseStack, consumer, camera, model);
            mc.renderBuffers().bufferSource().endBatch(RenderType.solid());
        }
    }
}
