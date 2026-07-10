package io.github.pouffy.rustic.core.item;

import io.github.pouffy.rustic.init.RusticTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.Random;

public class DisplayedItemStack {
    public static final DisplayedItemStack EMPTY = new DisplayedItemStack();

    private static final Random R = new Random();

    public ItemStack stack;
    public int angle;

    private DisplayedItemStack() {
        this.stack = ItemStack.EMPTY;
    }

    public DisplayedItemStack(ItemStack stack) {
        this.stack = stack;
        boolean centered = stack.is(RusticTags.RENDER_UPRIGHT);
        this.angle = centered ? 180 : R.nextInt(360);
    }

    public boolean isEmpty() {
        return this.stack.isEmpty();
    }

    public DisplayedItemStack copy() {
        DisplayedItemStack copy = new DisplayedItemStack(stack.copy());
        copy.angle = angle;
        return copy;
    }

    public CompoundTag serializeNBT(HolderLookup.Provider registries, CompoundTag nbt) {
        CompoundTag tag = new CompoundTag();
        tag.put("Item", stack.save(registries, nbt));
        tag.putInt("Angle", angle);
        return tag;
    }

    public static Optional<DisplayedItemStack> read(HolderLookup.Provider registries, CompoundTag nbt) {
        Optional<ItemStack> itemStack = ItemStack.parse(registries, nbt.getCompound("Item"));
        if (itemStack.isEmpty()) {
            return Optional.empty();
        }
        DisplayedItemStack stack = new DisplayedItemStack(itemStack.get());
        stack.angle = nbt.getInt("Angle");
        return Optional.of(stack);
    }
}
