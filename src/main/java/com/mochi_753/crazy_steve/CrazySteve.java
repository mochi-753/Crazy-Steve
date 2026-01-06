package com.mochi_753.crazy_steve;

import com.mochi_753.crazy_steve.common.network.ModNetwork;
import com.mochi_753.crazy_steve.common.register.ModItems;
import com.mojang.logging.LogUtils;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(CrazySteve.MOD_ID)
public class CrazySteve {
    public static final String MOD_ID = "crazy_steve";
    public static final Logger LOGGER = LogUtils.getLogger();

    @SuppressWarnings("removal")
    public CrazySteve() {
        this(FMLJavaModLoadingContext.get());
    }

    public CrazySteve(FMLJavaModLoadingContext context) {
        ModNetwork.init();
        ModItems.register(context.getModEventBus());
    }
}
