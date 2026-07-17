package io.github.pouffy.agrestic.common.block.entity;

import io.github.pouffy.agrestic.common.data.EvaporationBoosterManager;
import io.github.pouffy.agrestic.common.recipe.EvaporatingBasinRecipe;
import io.github.pouffy.agrestic.common.recipe.EvaporatingRecipeInput;
import io.github.pouffy.agrestic.core.block.ILightEmitting;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.fluid.transfer.FluidTransferHelper;
import io.github.pouffy.agrestic.core.item.DisplayedItemContainer;
import io.github.pouffy.agrestic.core.recipe.RecipeSearch;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import io.github.pouffy.agrestic.init.AgresticRecipeTypes;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class EvaporatingBasinBlockEntity extends BlockEntity {
    @Getter
    protected final AgresticFluidTank tank;
    @Getter
    protected final DisplayedItemContainer container;
    private float progress = 0;

    public EvaporatingBasinBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.EVAPORATING_BASIN.get(), pos, blockState);
        tank = new AgresticFluidTank(getCapacity(), this::onFluidStackChanged);
        container = new DisplayedItemContainer(1, (stack) -> this.markUpdated()).forbidInsertion();
    }

    protected void onFluidStackChanged(FluidStack newFluids) {
        if (level == null) return;
        if (tank != null) {
            tank.setCapacity(getCapacity());
            if (tank.getSpace() < 0) tank.drain(-tank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        ILightEmitting.updateLight(this, tank);
        markUpdated();
    }

    public int getCapacity() {
        return 6000;
    }

    public void serverTick() {
        if (level == null) return;
        FluidStack fluid = getTank().getFluid();
        long amt = getTank().getFluidAmount();

        if (fluid.isEmpty() || amt == 0) {
            this.progress = 0;
            return;
        }

        ItemStack storedItem = getContainer().getStackInSlot(0);
        var input = new EvaporatingRecipeInput(fluid, storedItem);
        EvaporatingBasinRecipe recipe = RecipeSearch.search(level, AgresticRecipeTypes.EVAPORATING_BASIN.get()).findRecipe(input);

        if (recipe == null) {
            this.progress = 0;
            return;
        }

        BlockPos below = worldPosition.below();
        BlockState belowState = level.getBlockState(below);
        float boosted = EvaporationBoosterManager.INSTANCE.getMultiplier(belowState);
        float tickIncrement = 1 * boosted;

        this.progress += tickIncrement;
        int craftTime = recipe.getTime();

        if (this.progress >= craftTime) {
            long extracted = getTank().drain(getFluidStack().copyWithAmount(recipe.getInput().amount()), IFluidHandler.FluidAction.SIMULATE).getAmount();
            if (extracted == recipe.getInput().amount()) {
                getTank().drain(getFluidStack().copyWithAmount(recipe.getInput().amount()), IFluidHandler.FluidAction.EXECUTE);
                this.progress = 0;
                this.spawnOrStore(recipe.getResultItem(level.registryAccess()).copy());
                level.playSound(null, worldPosition, SoundEvents.WET_SPONGE_DRIES, SoundSource.BLOCKS, 1.0F, 1.0F);
                level.sendBlockUpdated(worldPosition, getBlockState(), getBlockState(), Block.UPDATE_NEIGHBORS);
            } else {
                this.progress = 0;
            }
        }
        markUpdated();
    }

    private void spawnOrStore(ItemStack stack) {
        ItemStack slot = getContainer().getStackInSlot(0);

        if (slot.isEmpty()) {
            getContainer().setStackInSlot(0, stack);
        } else if (ItemStack.isSameItemSameComponents(slot, stack) && slot.getCount() + stack.getCount() <= slot.getMaxStackSize()) {
            slot.grow(stack.getCount());
        } else {
            assert level != null;
            Block.popResourceFromFace(level, worldPosition, Direction.UP, stack);
        }
        markUpdated();
    }

    public void clientTick() {

    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, Direction side) {
        if (level == null) return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
        ItemStack heldItem = player.getItemInHand(hand);
        if (heldItem != ItemStack.EMPTY) {
            if (FluidTransferHelper.interactWithTank(level, worldPosition, player, hand, side, side)) {
                return ItemInteractionResult.sidedSuccess(level.isClientSide);
            }
        }
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult interactEmpty(Player player) {
        if (level == null) return InteractionResult.PASS;
        if (player.isShiftKeyDown() && this.getFluidStack().getAmount() > 0) {
            FluidStack drained = this.tank.drain(getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            SoundEvent soundevent = FluidTransferHelper.getEmptySound(drained);
            if (soundevent != null) {
                level.playSound(null, this.worldPosition, soundevent, SoundSource.BLOCKS, 1F, 1F);
            }
            FluidTransferHelper.displayDrained(player, drained);
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

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.EVAPORATING_BASIN.get(), (be, context) -> be.tank);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AgresticBlockEntities.EVAPORATING_BASIN.get(), (be, context) -> be.container);
    }

    @Override
    public final void setRemoved() {
        super.setRemoved();
        invalidateCapabilities();
    }

    public FluidStack getFluidStack() {
        var inv = getTank();
        return inv.getFluid();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ILightEmitting.updateLight(this, this.tank.readFromNBT(registries, tag.getCompound("Tank")));
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
        this.progress = tag.getFloat("Progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
        tag.put("Container", this.container.serializeNBT(registries));
        tag.putFloat("Progress", this.progress);
    }

    private void markUpdated() {
        this.setChanged();
        assert this.getLevel() != null;
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
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
