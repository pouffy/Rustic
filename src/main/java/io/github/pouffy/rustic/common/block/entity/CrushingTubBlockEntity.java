package io.github.pouffy.rustic.common.block.entity;

import io.github.pouffy.rustic.core.fluid.RusticFluidTank;
import io.github.pouffy.rustic.core.fluid.transfer.FluidTransferHelper;
import io.github.pouffy.rustic.core.item.DisplayedItemContainer;
import io.github.pouffy.rustic.core.recipe.RecipeSearch;
import io.github.pouffy.rustic.init.RusticBlockEntities;
import io.github.pouffy.rustic.init.RusticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.SingleRecipeInput;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;

public class CrushingTubBlockEntity extends BlockEntity {

    @Getter
    protected final RusticFluidTank tank;
    @Getter
    protected final DisplayedItemContainer container;


    public CrushingTubBlockEntity(BlockPos pos, BlockState blockState) {
        super(RusticBlockEntities.CRUSHING_TUB.get(), pos, blockState);
        tank = new RusticFluidTank(getCapacity(), this::onFluidStackChanged).forbidInsertion();
        container = new DisplayedItemContainer(1) {
            @Override
            protected void onContentsChanged(int slot) {
                markUpdated();
            }
        };
    }

    protected void onFluidStackChanged(FluidStack newFluids) {
        if (level == null) return;
        if (tank != null) {
            tank.setCapacity(getCapacity());
            if (tank.getSpace() < 0) tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }

        markUpdated();
    }

    public int getCapacity() {
        return 8000;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, RusticBlockEntities.CRUSHING_TUB.get(), (be, context) -> be.tank);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, RusticBlockEntities.CRUSHING_TUB.get(), (be, context) -> be.container);
    }

    @Override
    public final void setRemoved() {
        super.setRemoved();
        invalidateCapabilities();
    }

    public void crush() {
        if (level == null) return;
        boolean noRecipe = false;
        if (hasStack()) {
            ItemStack stack = getStack();
            var input = new SingleRecipeInput(stack);
            var recipe = RecipeSearch.search(level, RusticRecipeTypes.CRUSHING_TUB.get()).findRecipe(input);
            if (recipe != null) {
                FluidStack output = recipe.finish(input, level.registryAccess());
                if (this.getFluidStack().getAmount() <= this.getCapacity() - output.getAmount()) {
                    if (!this.level.isClientSide) {
                        this.tank.forceFill(output, IFluidHandler.FluidAction.EXECUTE);
                        this.container.extractItem(0, 1, false);
                        ItemStack by = recipe.getByproduct().copy();
                        if (!by.isEmpty()) {
                            Block.popResource(level, worldPosition, by);
                        }
                        this.level.playSound(null, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, SoundEvents.SLIME_BLOCK_FALL, SoundSource.BLOCKS, 0.5F, this.level.random.nextFloat() * 0.1F + 0.9F);
                        level.updateNeighbourForOutputSignal(worldPosition, getBlockState().getBlock());
                        setChanged();
                    } else {
                        RandomSource random = this.level.random;
                        for (int i = 0; i < random.nextInt(8) + 8; ++i) {
                            ItemParticleOption particleOption = new ItemParticleOption(ParticleTypes.ITEM, stack);
                            this.level.addParticle(particleOption, this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5, ((random.nextDouble() * 0.1) - 0.05), ((random.nextDouble() * 0.05) + 0.05), ((random.nextDouble() * 0.1) - 0.05));
                        }
                    }
                }
            }
        }
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, Direction side) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem != ItemStack.EMPTY) {
            if (FluidTransferHelper.interactWithContainer(level, worldPosition, this.tank, player, hand).didTransfer()) {
                markUpdated();
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            } else {
                if (this.container.getStackInSlot(0).isEmpty()) {
                    player.setItemInHand(hand, this.container.insertItem(0, heldItem, false));
                    markUpdated();
                    return ItemInteractionResult.sidedSuccess(level.isClientSide);
                } else {
                    if (this.container.getStackInSlot(0).is(heldItem.getItem())) {
                        player.setItemInHand(hand, this.container.insertItem(0, heldItem, false));
                        markUpdated();
                        return ItemInteractionResult.sidedSuccess(level.isClientSide);
                    } else return ItemInteractionResult.FAIL;
                }
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult interactEmpty(Player player) {
        if (level == null) return InteractionResult.PASS;
        if (player.isShiftKeyDown() && this.getFluidStack().getAmount() > 0) {
            FluidStack drained = this.tank.drain(getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            SoundEvent soundevent = drained.getFluidType().getSound(drained, SoundActions.BUCKET_EMPTY);
            if (soundevent != null) {
                level.playSound(null, this.worldPosition, soundevent, SoundSource.BLOCKS, 1F, 1F);
            }
            markUpdated();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        if (!getStack().isEmpty() && !level.isClientSide) {
            level.addFreshEntity(new ItemEntity(level, player.getX(), player.getY(), player.getZ(), getStack()));
            container.setStackInSlot(0, ItemStack.EMPTY);
            markUpdated();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public FluidStack getFluidStack() {
        var inv = getTank();
        return inv == null ? FluidStack.EMPTY : inv.getFluid();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.tank.readFromNBT(registries, tag.getCompound("Tank"));
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
        tag.put("Container", this.container.serializeNBT(registries));
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public boolean hasStack() {
        return !getStack().isEmpty();
    }

    public ItemStack getStack() {
        return container.getStackInSlot(0);
    }
}
