package io.github.pouffy.agrestic.core.block;

import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.IFluidTank;

import java.util.function.ToIntFunction;

public interface ILightEmitting {
    IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);
    ToIntFunction<BlockState> LIGHT_GETTER = state -> state.getValue(ILightEmitting.LIGHT);

    static void updateLight(BlockEntity be, IFluidTank tank) {
        Level level = be.getLevel();
        if (level != null && !level.isClientSide) {
            FluidStack fluid = tank.getFluid();
            int light = fluid.isEmpty() ? 0 : fluid.getFluid().getFluidType().getLightLevel(fluid);
            BlockState state = be.getBlockState();
            if (light != state.getValue(ILightEmitting.LIGHT)) {
                level.setBlock(be.getBlockPos(), state.setValue(ILightEmitting.LIGHT, light), Block.UPDATE_CLIENTS);
            }
        }
    }
}
