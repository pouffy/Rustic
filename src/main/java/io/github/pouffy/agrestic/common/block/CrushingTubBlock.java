package io.github.pouffy.agrestic.common.block;

import com.mojang.serialization.MapCodec;
import io.github.pouffy.agrestic.common.block.entity.CrushingTubBlockEntity;
import io.github.pouffy.agrestic.core.block.ILightEmitting;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.Nullable;

public class CrushingTubBlock extends BaseEntityBlock implements ILightEmitting {
    public static final MapCodec<CrushingTubBlock> CODEC = simpleCodec(CrushingTubBlock::new);

    public CrushingTubBlock(Properties properties) {
        super(properties);
        registerDefaultState(defaultBlockState().setValue(LIGHT, 0));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(LIGHT);
    }

    @Override
    protected MapCodec<? extends BaseEntityBlock> codec() {
        return CODEC;
    }

    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(Block.box(0, 0, 0, 16, 9, 16), Block.box(2, 2, 2, 14, 9, 14), BooleanOp.ONLY_FIRST);
    }

    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return Shapes.join(Block.box(0, 0, 0, 16, 9, 16), Block.box(2, 5, 2, 14, 9, 14), BooleanOp.ONLY_FIRST);
    }

    @Override
    public @Nullable BlockEntity newBlockEntity(BlockPos blockPos, BlockState blockState) {
        return new CrushingTubBlockEntity(blockPos, blockState);
    }

    @Override
    public void fallOn(Level level, BlockState state, BlockPos pos, Entity entity, float fallDistance) {
        if (level.getBlockEntity(pos) instanceof CrushingTubBlockEntity crushingTubBlockEntity && fallDistance > 0.5) {
            crushingTubBlockEntity.crush();
        }
    }

    @Override
    protected InteractionResult useWithoutItem(BlockState state, Level level, BlockPos pos, Player player, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CrushingTubBlockEntity crushingTubBlockEntity) {
            return crushingTubBlockEntity.interactEmpty(player);
        }
        return InteractionResult.PASS;
    }

    @Override
    protected ItemInteractionResult useItemOn(ItemStack stack, BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult hitResult) {
        if (level.getBlockEntity(pos) instanceof CrushingTubBlockEntity crushingTubBlockEntity) {
            return crushingTubBlockEntity.interact(player, hand, hitResult.getDirection());
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (newState.getBlock() instanceof CrushingTubBlock) {
            return;
        }

        if (level.getBlockEntity(pos) instanceof CrushingTubBlockEntity blockEntity && blockEntity.hasStack()) {
            level.addFreshEntity(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, blockEntity.getStack()));
        }

        super.onRemove(state, level, pos, newState, isMoving);
    }
}
