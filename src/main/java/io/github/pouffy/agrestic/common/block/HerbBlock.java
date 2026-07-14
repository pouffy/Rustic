package io.github.pouffy.agrestic.common.block;

import io.github.pouffy.agrestic.init.AgresticTags;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.neoforge.common.CommonHooks;

import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

public abstract class HerbBlock extends CropBlock {
    public static final IntegerProperty AGE = IntegerProperty.create("age", 0, 6);
    private static final VoxelShape[] SHAPE_TO_AGE = new VoxelShape[]{
            Block.box(6.0, 0.0, 6.0, 10.0, 5.0, 10.0),
            Block.box(5.0, 0.0, 5.0, 11.0, 6.0, 11.0),
            Block.box(4.0, 0.0, 4.0, 12.0, 8.0, 12.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 11.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 12.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 13.0, 13.0),
            Block.box(3.0, 0.0, 3.0, 13.0, 14.0, 13.0),
    };
    public List<Predicate<BlockState>> survivalPredicates = null;
    public boolean prickly = false;
    public boolean dense = false;
    public int minimumLight = 9;

    public HerbBlock(Properties properties) {
        super(properties.offsetType(BlockBehaviour.OffsetType.XZ));
        this.defaultBlockState().setValue(this.getAgeProperty(), 0);
    }

    public HerbBlock(Properties properties, Predicate<BlockState> placementCheck, boolean prickly, boolean dense) {
        this(properties);
        this.survivalPredicates = List.of(placementCheck);
        this.prickly = prickly;
        this.dense = dense;
    }

    public HerbBlock(Properties properties, int minimumLight) {
        this(properties);
        this.minimumLight = minimumLight;
    }

    public HerbBlock(Properties properties, boolean prickly, boolean dense) {
        this(properties);
        this.prickly = prickly;
        this.dense = dense;
    }

    public void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(AGE);
    }

    public abstract Supplier<? extends Item> getHerb();

    @Override
    protected boolean mayPlaceOn(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        boolean regularChecks = pState.is(AgresticTags.FERTILE_SOILS.blockTag()) || pState.is(AgresticTags.HERBS_CAN_SURVIVE_ON);
        if(survivalPredicates != null) {
            for(Predicate<BlockState> predicate : survivalPredicates) {
                if(predicate.test(pState)) {
                    return true;
                }
            }
        }
        return regularChecks;
    }

    @Override
    protected void entityInside(BlockState state, Level level, BlockPos pos, Entity entity) {
        if (this.dense) {
            if (!entity.getType().is(AgresticTags.DENSE_HERB_UNAFFECTED)) {
                entity.makeStuckInBlock(state, new Vec3(0.8F, 0.75F, 0.8F));
            }
        }
        if (this.prickly) {
            if (!entity.getType().is(AgresticTags.PRICKLY_HERB_UNAFFECTED)) {
                if (!level.isClientSide() && state.getValue(AGE) > 0 && (entity.xOld != entity.getX() || entity.zOld != entity.getZ())) {
                    double d = Math.abs(entity.getX() - entity.xOld);
                    double e = Math.abs(entity.getZ() - entity.zOld);
                    if (d >= (double) 0.003F || e >= (double) 0.003F) {
                        entity.hurt(level.damageSources().sweetBerryBush(), 1.0F);
                    }
                }
            }
        }
    }

    public void randomTick(BlockState state, ServerLevel level, BlockPos pos, RandomSource random) {
        if (level.isAreaLoaded(pos, 1)) {
            if (level.getRawBrightness(pos, 0) >= minimumLight) {
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
        return SHAPE_TO_AGE[this.getAge(state)];
    }

    public ItemStack getCloneItemStack(LevelReader level, BlockPos pos, BlockState state) {
        return new ItemStack(this.getHerb().get());
    }

    public IntegerProperty getAgeProperty() {
        return AGE;
    }

    public int getMaxAge() {
        return 6;
    }

    public int getAge(BlockState state) {
        return state.getValue(this.getAgeProperty());
    }

    public BlockState withAge(int age) {
        return this.defaultBlockState().setValue(this.getAgeProperty(), age);
    }


}
