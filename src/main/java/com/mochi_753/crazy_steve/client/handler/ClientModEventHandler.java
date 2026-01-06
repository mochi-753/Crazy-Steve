package com.mochi_753.crazy_steve.client.handler;

import com.mochi_753.crazy_steve.CrazySteve;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = CrazySteve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEventHandler {
    @SuppressWarnings("removal")
    private static final ResourceLocation CONTINUE_PIPE_LOCATION = new ResourceLocation(CrazySteve.MOD_ID, "custom/continue_pipe");

    @SubscribeEvent
    public static void onRegisterAdditional(ModelEvent.RegisterAdditional event) {
        CrazySteve.LOGGER.info("Registering custom models");
        event.register(CONTINUE_PIPE_LOCATION);
    }
}
