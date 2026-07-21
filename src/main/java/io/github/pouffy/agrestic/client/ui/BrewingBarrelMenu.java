package io.github.pouffy.agrestic.client.ui;

import io.github.pouffy.agrestic.client.AgresticSprites;
import io.github.pouffy.agrestic.client.ui.slot.FluidTransferInputSlot;
import io.github.pouffy.agrestic.client.ui.slot.ResultSlot;
import io.github.pouffy.agrestic.common.block.entity.BrewingBarrelBlockEntity;
import io.github.pouffy.agrestic.common.item.BoozeBottleItem;
import io.github.pouffy.agrestic.init.AgresticMenuTypes;
import lombok.Getter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.entity.BlockEntity;

import java.util.Objects;

public class BrewingBarrelMenu extends AbstractContainerMenu {
    private final Container inventory;
    private final ContainerData data;
    @Getter
    public final BrewingBarrelBlockEntity blockEntity;

    public BrewingBarrelMenu(int syncId, Inventory playerInventory, final FriendlyByteBuf data) {
        this(syncId, playerInventory, getBlockEntity(playerInventory, data), new SimpleContainerData(2));
    }

    public BrewingBarrelMenu(int syncId, Inventory playerInventory, BrewingBarrelBlockEntity blockEntity, ContainerData data) {
        super(AgresticMenuTypes.BREWING_BARREL.get(), syncId);
        this.inventory = blockEntity;
        this.blockEntity = blockEntity;
        this.data = data;

        this.addSlot(new FluidTransferInputSlot(blockEntity, 0, 3, 62, 7, blockEntity.getInputTank(), playerInventory.player));
        this.addSlot(new FluidTransferInputSlot(blockEntity, 1, 4, 116, 7, blockEntity.getResultTank(), playerInventory.player));
        this.addSlot(new FluidTransferInputSlot(blockEntity, 2, 5, 26, 15, blockEntity.getAuxiliaryTank(), playerInventory.player));

        this.addSlot(new ResultSlot(inventory, 3, 62, 63));
        this.addSlot(new ResultSlot(inventory, 4, 116, 63));
        this.addSlot(new ResultSlot(inventory, 5, 26, 55));

        addPlayerInventory(playerInventory);
        addPlayerHotbar(playerInventory);

        this.addDataSlots(data);
    }

    private static BrewingBarrelBlockEntity getBlockEntity(final Inventory playerInventory, final FriendlyByteBuf data) {
        Objects.requireNonNull(playerInventory, "playerInventory cannot be null");
        Objects.requireNonNull(data, "data cannot be null");
        final BlockEntity blockEntity = playerInventory.player.level().getBlockEntity(data.readBlockPos());
        if (blockEntity instanceof BrewingBarrelBlockEntity brewingBarrel) {
            return brewingBarrel;
        }
        throw new IllegalStateException("Block entity is not correct! " + blockEntity);
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = this.slots.get(index);
        if (slot.hasItem()) {
            ItemStack originalStack = slot.getItem();
            if (index < inventory.getContainerSize()) {
                if (!this.moveItemStackTo(originalStack, inventory.getContainerSize(), slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (originalStack.getItem() instanceof BucketItem) {
                    if (!this.moveItemStackTo(originalStack, 0, 1, false))
                        return ItemStack.EMPTY;
                }
                else if (originalStack.is(Items.GLASS_BOTTLE)) {
                    if (!this.moveItemStackTo(originalStack, 1, 2, false))
                        return ItemStack.EMPTY;
                }
                else if ((originalStack.getItem() instanceof BoozeBottleItem)) {
                    if (!this.moveItemStackTo(originalStack, 2, 3, false))
                        return ItemStack.EMPTY;
                }
                else if (!this.moveItemStackTo(originalStack, 0, 3, false)) {
                    return ItemStack.EMPTY;
                }
            }
            if (originalStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean stillValid(Player player) {
        return inventory.stillValid(player);
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; i++) {
            for (int l = 0; l < 9; l++) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 85 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; i++) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 143));
        }
    }

    public int getBrewingTime() {
        return this.data.get(0);
    }

    public int getScaledArrowProgress() {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowProgressWidth = AgresticSprites.ARROW.width;
        return (maxProgress == 0 || progress == 0) ? 0 : progress * arrowProgressWidth / maxProgress;
    }

    public int getScaledAuxArrowProgress() {
        if (this.blockEntity.getAuxiliaryTank().isEmpty()) return 0;
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);
        int arrowProgressWidth = AgresticSprites.TINY_ARROW.width;
        return (maxProgress == 0 || progress == 0) ? 0 : progress * arrowProgressWidth / maxProgress;
    }
}
