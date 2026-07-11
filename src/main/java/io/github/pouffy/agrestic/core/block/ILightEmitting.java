package io.github.pouffy.agrestic.core.block;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

import java.util.function.ToIntFunction;

public interface ILightEmitting {
    IntegerProperty LIGHT = IntegerProperty.create("light", 0, 15);
    ToIntFunction<BlockState> LIGHT_GETTER = state -> state.getValue(ILightEmitting.LIGHT);
}
