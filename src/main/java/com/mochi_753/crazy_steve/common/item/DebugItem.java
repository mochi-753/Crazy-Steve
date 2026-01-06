package com.mochi_753.crazy_steve.common.item;

import com.mochi_753.crazy_steve.common.entity.ContinuePipe;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

import javax.annotation.ParametersAreNonnullByDefault;

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
public class DebugItem extends Item {
    public DebugItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()) new ContinuePipe(pPlayer.position(), pPlayer.level().dimension());
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
