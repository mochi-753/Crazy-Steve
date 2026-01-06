package com.mochi_753.crazy_steve.common.entity;

import com.mochi_753.crazy_steve.CrazySteve;
import com.mochi_753.crazy_steve.common.network.ClientboundContinuePacket;
import com.mochi_753.crazy_steve.common.network.ModNetwork;
import com.mochi_753.crazy_steve.common.sound.SoundSequence;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Cat;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ContinuePipe {
    private static final int MAX_LIFETIME_TICKS = 100;
    private static final int SOUND_START_TICK = 40;
    private static final List<ContinuePipe> PIPES = new ArrayList<>();

    private final Vec3 position;
    private final ResourceKey<Level> dimension;
    private int tickCount;

    public ContinuePipe(Vec3 position, ResourceKey<Level> dimension) {
        this.position = position;
        this.dimension = dimension;
        this.tickCount = 0;

        PIPES.add(this);
        ModNetwork.CHANNEL.send(PacketDistributor.ALL.noArg(), new ClientboundContinuePacket(position, getDimension()));
    }

    public static void tickAll(TickEvent.ServerTickEvent event) {
        Iterator<ContinuePipe> iterator = PIPES.iterator();
        while (iterator.hasNext()) {
            ContinuePipe pipe = iterator.next();
            pipe.tick(event);
            if (pipe.isExpired()) iterator.remove();
        }
    }

    public static void clearAll() {
        PIPES.clear();
    }

    private static void prepareSounds(Vec3 position, ResourceKey<Level> dimension) {
        SoundSequence sequence = new SoundSequence(SoundSource.PLAYERS, position, dimension, 10.0F, 1.0F, SOUND_START_TICK);

        sequence.addQueue(0, harp(0.749F));
        sequence.addQueue(6, harp(0.749F));
        sequence.addQueue(8, harp(0.794F));
        sequence.addQueue(10, harp(0.890F));
        sequence.addQueue(16, harp(1.000F));
        for (int j = 0; j < 18; j++) {
            sequence.addQueue(22 + j, harp(0.890F));
        }
    }

    private static SoundSequence.SoundQueue harp(float pitch) {
        return new SoundSequence.SoundQueue(SoundEvents.NOTE_BLOCK_HARP.value(), 1.0F, pitch);
    }

    public void tick(TickEvent.ServerTickEvent event) {
        if (getTickCount() == SOUND_START_TICK) prepareSounds(getPosition(), getDimension());
        if (getTickCount() == 80) {
            ServerLevel level = event.getServer().getLevel(getDimension());
            if (level != null) {
                Cat cat = new Cat(EntityType.CAT, level);
                cat.moveTo(getPosition());
                level.addFreshEntity(cat);
            }
        }
        tickCount++;
    }

    public boolean isExpired() {
        return getTickCount() > MAX_LIFETIME_TICKS;
    }

    public Vec3 getPosition() {
        return position;
    }

    public ResourceKey<Level> getDimension() {
        return dimension;
    }

    public int getTickCount() {
        return tickCount;
    }

    @Mod.EventBusSubscriber(modid = CrazySteve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {
        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase == TickEvent.Phase.END) {
                ContinuePipe.tickAll(event);
            }
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            if (!event.getLevel().isClientSide()) {
                ContinuePipe.clearAll();
            }
        }
    }
}
