package com.mochi_753.crazy_steve.common.sound;

import com.mochi_753.crazy_steve.CrazySteve;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.level.LevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public class SoundSequence {
    private static final List<SoundSequence> SEQUENCES = new ArrayList<>();
    private final Map<Integer, SoundQueue> queues = new HashMap<>();
    private final SoundSource source;
    private final Vec3 position;
    private final ResourceKey<Level> dimension;
    private final float volume;
    private final float pitch;
    private final int lifespan;
    private int tickCount;

    public SoundSequence(SoundSource source, Vec3 position, ResourceKey<Level> dimension, float volume, float pitch, int lifespan) {
        this.source = source;
        this.position = position;
        this.dimension = dimension;
        this.volume = volume;
        this.pitch = pitch;
        this.lifespan = lifespan;
        this.tickCount = 0;

        SEQUENCES.add(this);
    }

    public static void tickAll(TickEvent.ServerTickEvent event) {
        Iterator<SoundSequence> iterator = SEQUENCES.iterator();
        while (iterator.hasNext()) {
            SoundSequence sequence = iterator.next();
            sequence.tick(event);
            if (sequence.isExpired()) iterator.remove();
        }
    }

    public static void clearAll() {
        SEQUENCES.clear();
    }

    private void tick(TickEvent.ServerTickEvent event) {
        ServerLevel level = event.getServer().getLevel(getDimension());
        if (level != null) {
            SoundQueue queue = getQueues().get(getTickCount());
            if (queue != null && queue.soundEvent() != null) play(level, queue);
        }
        tickCount++;
    }

    private void play(Level level, SoundQueue queue) {
        double x = getPosition().x();
        double y = getPosition().y();
        double z = getPosition().z();

        level.playSound(null, x, y, z, queue.soundEvent(), getSource(), getVolume() * queue.volume(), getPitch() * queue.pitch());
        level.playSound(null, x, y, z, queue.soundEvent(), getSource(), getVolume() * queue.volume(), getPitch() * queue.pitch() * 2.0F);
    }

    private boolean isExpired() {
        return getTickCount() > getLifespan();
    }

    public void addQueue(int delay, SoundQueue queue) {
        getQueues().put(getTickCount() + delay, queue);
    }

    public Map<Integer, SoundQueue> getQueues() {
        return queues;
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

    public float getPitch() {
        return pitch;
    }

    public float getVolume() {
        return volume;
    }

    public SoundSource getSource() {
        return source;
    }

    public int getLifespan() {
        return lifespan;
    }

    public record SoundQueue(SoundEvent soundEvent, float volume, float pitch) {
    }

    @Mod.EventBusSubscriber(modid = CrazySteve.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
    public static class EventHandler {
        @SubscribeEvent
        public static void onServerTick(TickEvent.ServerTickEvent event) {
            if (event.phase.equals(TickEvent.Phase.END)) SoundSequence.tickAll(event);
        }

        @SubscribeEvent
        public static void onLevelUnload(LevelEvent.Unload event) {
            if (!event.getLevel().isClientSide()) SoundSequence.clearAll();
        }
    }
}
