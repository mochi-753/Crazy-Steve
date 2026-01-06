package com.mochi_753.crazy_steve.common.network;

import com.mochi_753.crazy_steve.CrazySteve;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class ModNetwork {
    private static final String PROTOCOL_VERSION = "1";

    @SuppressWarnings("removal")
    public static final SimpleChannel CHANNEL = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(CrazySteve.MOD_ID, "main"),
            () -> PROTOCOL_VERSION, PROTOCOL_VERSION::equals, PROTOCOL_VERSION::equals
    );

    public static void init() {
        int id = 0;
        CHANNEL.messageBuilder(ClientboundContinuePacket.class, id++, NetworkDirection.PLAY_TO_CLIENT)
                .encoder(ClientboundContinuePacket::encode)
                .decoder(ClientboundContinuePacket::decode)
                .consumerMainThread(ClientboundContinuePacket::handle)
                .add();
    }
}
