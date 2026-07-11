package io.github.pouffy.agrestic.common.block;

import io.github.pouffy.agrestic.init.AgresticBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.ItemAbilities;
import net.neoforged.neoforge.common.ItemAbility;
import org.jetbrains.annotations.Nullable;

public class LogBlock extends RotatedPillarBlock {
    public LogBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    public boolean isFlammable(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return true;
    }

    public int getFlammability(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    public int getFireSpreadSpeed(BlockState state, BlockGetter level, BlockPos pos, Direction direction) {
        return 5;
    }

    public @Nullable BlockState getToolModifiedState(BlockState state, UseOnContext context, ItemAbility itemAbility, boolean simulate) {
        if (itemAbility.equals(ItemAbilities.AXE_STRIP)) {
            BlockState checkedState = AgresticBlocks.OLIVE.checkLogStripping(state);
            if (checkedState != null) {
                return checkedState;
            }

            checkedState = AgresticBlocks.IRONWOOD.checkLogStripping(state);
            if (checkedState != null) {
                return checkedState;
            }
        }

        return super.getToolModifiedState(state, context, itemAbility, simulate);
    }
}
