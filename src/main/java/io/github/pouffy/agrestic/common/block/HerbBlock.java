package io.github.pouffy.agrestic.common.block;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.function.Supplier;

public abstract class HerbBlock extends CropBlock {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_3;
    private static final VoxelShape[] SHAPES = new VoxelShape[] {
            box(0.0D, 0.0D, 0.0D, 16.0D, 2.0D, 16.0D),
            box(0.0D, 0.0D, 0.0D, 16.0D, 4.0D, 16.0D),
            box(0.0D, 0.0D, 0.0D, 16.0D, 8.0D, 16.0D),
            box(0.0D, 0.0D, 0.0D, 16.0D, 14.0D, 16.0D)
    };

    public HerbBlock(Properties properties) {
        super(properties.offsetType(BlockBehaviour.OffsetType.XZ));
        this.defaultBlockState().setValue(this.getAgeProperty(), 0);
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public abstract Supplier<? extends Item> getHerb();

    public int getMinLight() {
        return 9;
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (level.getRawBrightness(pos, 0) >= getMinLight()) {
                int i = this.getAge(state);
                if (i < this.getMaxAge()) {
                    float f = getGrowthSpeed(state, level, pos);
                    if (CommonHooks.canCropGrow(level, pos, state, random.nextInt((int)(25.0F / f) + 1) == 0)) {
                        level.setBlock(pos, this.getStateForAge(i + 1), 2);
                        CommonHooks.fireCropGrowPost(level, pos, state);
                    }
                }
            }

        }
    }

    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return SHAPES[this.getAge(state)];
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getHerb().get());
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 3;
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }


}
