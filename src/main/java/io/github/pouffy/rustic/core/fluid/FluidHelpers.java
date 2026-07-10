package io.github.pouffy.rustic.core.fluid;

import com.pouffydev.krystal_core.KrystalCore;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import net.neoforged.neoforge.fluids.capability.wrappers.FluidBucketWrapper;

public class FluidHelpers {
    public static boolean isFluidHandlerValid(ItemStack stack, IFluidHandlerItem fluidHandler) {
        if (fluidHandler.getClass() == FluidBucketWrapper.class) {
            Item item = stack.getItem();
            if (item.getClass() != BucketItem.class && !(item instanceof MilkBucketItem)) {
                return false;
            }
        }
        return true;
    }

    public static boolean canItemBeFilled(ItemStack stack) {
        if (stack.getItem() == Items.GLASS_BOTTLE)
            return true;
        if (stack.getItem() == Items.MILK_BUCKET)
            return false;

        IFluidHandlerItem capability = stack.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return false;
        if (!isFluidHandlerValid(stack, capability))
            return false;
        for (int i = 0; i < capability.getTanks(); i++) {
            if (capability.getFluidInTank(i)
                    .getAmount() < capability.getTankCapacity(i))
                return true;
        }
        return false;
    }

    public static boolean canFillGlassBottleInternally(FluidStack availableFluid) {
        Fluid fluid = availableFluid.getFluid();
        if (fluid.isSame(Fluids.WATER))
            return true;
        if (fluid.isSame(KrystalCore.HONEY.get()))
            return true;
        return false;
    }

    public static ItemStack fillItem(int requiredAmount, ItemStack stack, FluidStack availableFluid) {
        FluidStack toFill = availableFluid.copy();
        toFill.setAmount(requiredAmount);
        availableFluid.shrink(requiredAmount);

        if (stack.getItem() == Items.GLASS_BOTTLE && canFillGlassBottleInternally(toFill)) {
            ItemStack fillBottle;
            Fluid fluid = toFill.getFluid();
            if (isWater(fluid))
                fillBottle = PotionContents.createItemStack(Items.POTION, Potions.WATER);
            else
                fillBottle = Items.HONEY_BOTTLE.getDefaultInstance();
            stack.shrink(1);
            return fillBottle;
        }
        ItemStack split = stack.copy();
        split.setCount(1);
        IFluidHandlerItem capability = split.getCapability(Capabilities.FluidHandler.ITEM);
        if (capability == null)
            return ItemStack.EMPTY;
        capability.fill(toFill, IFluidHandler.FluidAction.EXECUTE);
        ItemStack container = capability.getContainer()
                .copy();
        stack.shrink(1);
        return container;
    }

    public static FluidStack copyStackWithAmount(FluidStack fs, int amount) {
        if (amount <= 0)
            return FluidStack.EMPTY;
        if (fs.isEmpty())
            return FluidStack.EMPTY;
        FluidStack copy = fs.copy();
        copy.setAmount(amount);
        return copy;
    }

    public static boolean isWater(Fluid fluid) {
        return convertToStill(fluid) == Fluids.WATER;
    }

    public static boolean isLava(Fluid fluid) {
        return convertToStill(fluid) == Fluids.LAVA;
    }

    public static Fluid convertToFlowing(Fluid fluid) {
        if (fluid == Fluids.WATER)
            return Fluids.FLOWING_WATER;
        if (fluid == Fluids.LAVA)
            return Fluids.FLOWING_LAVA;
        if (fluid instanceof BaseFlowingFluid)
            return ((BaseFlowingFluid) fluid).getFlowing();
        return fluid;
    }

    public static Fluid convertToStill(Fluid fluid) {
        if (fluid == Fluids.FLOWING_WATER)
            return Fluids.WATER;
        if (fluid == Fluids.FLOWING_LAVA)
            return Fluids.LAVA;
        if (fluid instanceof BaseFlowingFluid)
            return ((BaseFlowingFluid) fluid).getSource();
        return fluid;
    }
}
