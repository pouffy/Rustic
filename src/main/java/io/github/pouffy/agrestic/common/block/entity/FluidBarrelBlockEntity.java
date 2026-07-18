package io.github.pouffy.agrestic.common.block.entity;

import io.github.pouffy.agrestic.core.block.AgresticBlockEntity;
import io.github.pouffy.agrestic.core.block.ILightEmitting;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.fluid.FluidHelper;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.ItemInteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

public class FluidBarrelBlockEntity extends AgresticBlockEntity {
    @Getter
    protected final AgresticFluidTank tank;

    public FluidBarrelBlockEntity(BlockPos pos, BlockState blockState) {
        super(AgresticBlockEntities.FLUID_BARREL.get(), pos, blockState);
        tank = new AgresticFluidTank(getCapacity(), this::onFluidStackChanged);
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
        return 16000;
    }

    public ItemInteractionResult interact(Player player, InteractionHand hand, Direction side) {
        return ItemInteractionResult.PASS_TO_DEFAULT_BLOCK_INTERACTION;
    }

    public InteractionResult interactEmpty(Player player) {
        if (level == null) return InteractionResult.PASS;
        if (player.isShiftKeyDown() && this.getFluidStack().getAmount() > 0) {
            FluidStack drained = this.tank.drain(getCapacity(), IFluidHandler.FluidAction.EXECUTE);
            SoundEvent soundevent = FluidHelper.getEmptySound(drained);
            if (soundevent != null) {
                level.playSound(null, this.worldPosition, soundevent, SoundSource.BLOCKS, 1F, 1F);
            }
            FluidHelper.displayDrained(player, drained);
            markUpdated();
            return InteractionResult.sidedSuccess(level.isClientSide);
        }
        return InteractionResult.PASS;
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.FLUID_BARREL.get(), (be, context) -> be.tank);
    }

    public FluidStack getFluidStack() {
        var inv = getTank();
        return inv.getFluid();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        ILightEmitting.updateLight(this, this.tank.readFromNBT(registries, tag.getCompound("Tank")));
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Tank", this.tank.writeToNBT(registries, new CompoundTag()));
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }
}
