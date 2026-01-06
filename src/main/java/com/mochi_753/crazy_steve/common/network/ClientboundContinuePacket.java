package com.mochi_753.crazy_steve.common.network;

import com.mochi_753.crazy_steve.client.entity.ContinuePipeRenderer;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public record ClientboundContinuePacket(Vec3 position, ResourceKey<Level> dimension) {
    public static void encode(ClientboundContinuePacket packet, FriendlyByteBuf buf) {
        buf.writeDouble(packet.position().x());
        buf.writeDouble(packet.position().y());
        buf.writeDouble(packet.position().z());
        buf.writeResourceKey(packet.dimension());
    }

    public static ClientboundContinuePacket decode(FriendlyByteBuf buf) {
        Vec3 position = new Vec3(buf.readDouble(), buf.readDouble(), buf.readDouble());
        return new ClientboundContinuePacket(position, buf.readResourceKey(Registries.DIMENSION));
    }

    public static void handle(ClientboundContinuePacket packet, Supplier<NetworkEvent.Context> contextSupplier) {
        NetworkEvent.Context context = contextSupplier.get();
        context.enqueueWork(() -> DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> new ContinuePipeRenderer(packet.position(), packet.dimension())));
        context.setPacketHandled(true);
    }
}
