package io.github.pouffy.agrestic.core.block;

import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.item.AgresticItemContainer;
import lombok.Getter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.Container;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.Nameable;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public abstract class AgresticContainerBlockEntity extends AgresticBlockEntity implements Container, MenuProvider, Nameable {

    @Getter
    protected final AgresticItemContainer container;

    private Component customName;

    protected final ContainerData data;

    public AgresticContainerBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState blockState) {
        super(type, pos, blockState);
        this.container = newContainer();
        this.data = createContainerData();
    }

    public abstract AgresticItemContainer newContainer();

    public abstract ContainerData createContainerData();
    public abstract String getContainerName();

    @Override
    public void loadAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.loadAdditional(tag, registries);
        this.container.deserializeNBT(registries, tag.getCompound("Container"));
        if (customName != null) {
            tag.putString("CustomName", Component.Serializer.toJson(customName, registries));
        }
    }

    @Override
    public void saveAdditional(CompoundTag tag, HolderLookup.Provider registries) {
        super.saveAdditional(tag, registries);
        tag.put("Container", this.container.serializeNBT(registries));
        if (tag.contains("CustomName", 8)) {
            customName = Component.Serializer.fromJson(tag.getString("CustomName"), registries);
        }
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

    @Override
    public int getContainerSize() {
        return this.getContainer().getSlots();
    }

    @Override
    public boolean isEmpty() {
        return this.getContainer().isEmpty();
    }

    @Override
    public ItemStack getItem(int index) {
        return this.getContainer().getStackInSlot(index);
    }

    @Override
    public ItemStack removeItem(int index, int count) {
        ItemStack itemstack = this.getContainer().extractItem(index, count, false);
        if (!itemstack.isEmpty()) {
            this.markUpdated();
        }

        return itemstack;
    }

    @Override
    public ItemStack removeItemNoUpdate(int index) {
        return this.getContainer().extractItem(index, getContainer().getStackInSlot(index).getMaxStackSize(), false);
    }

    @Override
    public void setItem(int index, ItemStack itemStack) {
        this.getContainer().insertItem(index, itemStack, false);
        this.markUpdated();
    }

    @Override
    public boolean stillValid(Player player) {
        return Container.stillValidBlockEntity(this, player);
    }

    @Override
    public void clearContent() {
        this.getContainer().clear();
        this.markUpdated();
    }

    @Override
    public Component getName() {
        return customName != null ? customName : Component.translatable(Agrestic.location(getContainerName()).toLanguageKey("container"));
    }

    @Override
    public Component getDisplayName() {
        return getName();
    }

    @Override
    @Nullable
    public Component getCustomName() {
        return customName;
    }

    @Override
    protected void applyImplicitComponents(BlockEntity.DataComponentInput componentInput) {
        super.applyImplicitComponents(componentInput);
        this.customName = componentInput.get(DataComponents.CUSTOM_NAME);
    }

    @Override
    protected void collectImplicitComponents(DataComponentMap.Builder components) {
        super.collectImplicitComponents(components);
        components.set(DataComponents.CUSTOM_NAME, this.customName);
    }

    @Override
    public void removeComponentsFromTag(CompoundTag tag) {
        tag.remove("CustomName");
    }

    @Override
    public abstract @Nullable AbstractContainerMenu createMenu(int i, Inventory inventory, Player player);
}
