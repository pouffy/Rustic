package io.github.pouffy.agrestic.common.block.entity;

import io.github.pouffy.agrestic.common.recipe.BrewingBarrelRecipe;
import io.github.pouffy.agrestic.core.block.AgresticBlockEntity;
import io.github.pouffy.agrestic.core.fluid.AgresticFluidTank;
import io.github.pouffy.agrestic.core.fluid.CombinedFluidTank;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
import io.github.pouffy.agrestic.init.AgresticBlockEntities;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import java.util.ArrayList;
import java.util.List;

public class BrewingBarrelBlockEntity extends AgresticBlockEntity {

    @Getter
    protected final CombinedFluidTank tanks;
    @Getter
    protected final AgresticFluidTank inputTank;
    @Getter
    protected final AgresticFluidTank resultTank;
    @Getter
    protected final AgresticFluidTank auxiliaryTank;
    @Getter
    protected final AgresticItemContainer container;

    protected int progress;

    protected BrewingBarrelRecipe recipe = null;

    public BrewingBarrelBlockEntity(BlockPos pos, BlockState state) {
        super(AgresticBlockEntities.BREWING_BARREL.get(), pos, state);
        this.inputTank = new AgresticFluidTank(8000, this::onInputChanged);
        this.resultTank = new AgresticFluidTank(8000, this::onOutputChanged).forbidInsertion();
        this.auxiliaryTank = new AgresticFluidTank(1000, this::onAuxiliaryChanged);
        this.tanks = new CombinedFluidTank(this.inputTank, this.resultTank, this.auxiliaryTank);
        this.container = new AgresticItemContainer(6, this::onItemsChanged);
    }

    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.registerBlockEntity(Capabilities.FluidHandler.BLOCK, AgresticBlockEntities.BREWING_BARREL.get(), (be, context) -> be.tanks);
        event.registerBlockEntity(Capabilities.ItemHandler.BLOCK, AgresticBlockEntities.BREWING_BARREL.get(), (be, context) -> be.container);
    }

    protected void onInputChanged(FluidStack newFluids) {
        if (inputTank != null) {
            inputTank.setCapacity(8000);
            if (inputTank.getSpace() < 0) inputTank.drain(-inputTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    protected void onOutputChanged(FluidStack newFluids) {
        if (resultTank != null) {
            resultTank.setCapacity(8000);
            if (resultTank.getSpace() < 0) resultTank.drain(-resultTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    protected void onAuxiliaryChanged(FluidStack newFluids) {
        if (auxiliaryTank != null) {
            auxiliaryTank.setCapacity(1000);
            if (auxiliaryTank.getSpace() < 0) auxiliaryTank.drain(-auxiliaryTank.getSpace(), IFluidHandler.FluidAction.EXECUTE);
        }
        markUpdated();
    }

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.inputTank.readFromNBT(registries, tag.getCompound("InputTank"));
        this.resultTank.readFromNBT(registries, tag.getCompound("ResultTank"));
        this.auxiliaryTank.readFromNBT(registries, tag.getCompound("AuxiliaryTank"));
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
        this.progress = tag.getInt("Progress");
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("InputTank", this.inputTank.writeToNBT(registries, new CompoundTag()));
        tag.put("ResultTank", this.resultTank.writeToNBT(registries, new CompoundTag()));
        tag.put("AuxiliaryTank", this.auxiliaryTank.writeToNBT(registries, new CompoundTag()));
        tag.put("Container", this.container.serializeNBT(registries));
        tag.putInt("Progress", this.progress);
    }

    @Override
    public CompoundTag getUpdateTag(HolderLookup.Provider registries) {
        return this.saveWithoutMetadata(registries);
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    public List<ItemStack> getAllStacks() {
        List<ItemStack> stacks = new ArrayList<>();
        for (int i = 0; i < getContainer().getSlots(); i++) {
            ItemStack contained = getContainer().getStackInSlot(i);
            if (!contained.isEmpty()) {
                stacks.add(contained);
            }
        }
        return stacks;
    }
}
