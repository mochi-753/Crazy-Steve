package com.mochi_753.crazy_steve.common.register;

import com.mochi_753.crazy_steve.CrazySteve;
import com.mochi_753.crazy_steve.common.item.DebugItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@SuppressWarnings("unused")
public class ModItems {
    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(Registries.ITEM, CrazySteve.MOD_ID);

    public static final RegistryObject<Item> DEBUG = ITEMS.register("debug",
            () -> new DebugItem(new Item.Properties().stacksTo(1)));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
