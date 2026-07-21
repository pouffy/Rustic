package io.github.pouffy.agrestic.core.fluid;

import com.pouffydev.krystal_core.KrystalCore;
import com.pouffydev.krystal_core.content.item.HoneyBucketItem;
import io.github.pouffy.agrestic.core.item.AgresticBucketItem;
import lombok.Getter;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MilkBucketItem;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

@Getter
public class AgresticBucketWrapper implements IFluidHandlerItem {
    protected ItemStack container;

    public AgresticBucketWrapper(ItemStack container) {
        this.container = container;
    }

    public boolean canFillFluidType(FluidStack fluid) {
        if (!fluid.is(Fluids.WATER) && !fluid.is(Fluids.LAVA)) {
            return !fluid.getFluidType().getBucket(fluid).isEmpty();
        } else {
            return true;
        }
    }

    public FluidStack getFluid() {
        Item item = this.container.getItem();
        if (item instanceof AgresticBucketItem agresticBucketItem) {
            return new FluidStack(agresticBucketItem.content.get(), 1000);
        }  else {
            return item instanceof HoneyBucketItem && KrystalCore.HONEY.isBound() ? new FluidStack(KrystalCore.HONEY.get(), 1000) : FluidStack.EMPTY;
        }
    }

    protected void setFluid(FluidStack fluidStack) {
        if (fluidStack.isEmpty()) {
            this.container = new ItemStack(Items.BUCKET);
        } else {
            this.container = FluidUtil.getFilledBucket(fluidStack);
        }

    }

    public int getTanks() {
        return 1;
    }

    public FluidStack getFluidInTank(int tank) {
        return this.getFluid();
    }

    public int getTankCapacity(int tank) {
        return 1000;
    }

    public boolean isFluidValid(int tank, FluidStack stack) {
        return true;
    }

    public int fill(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 1000 && !(this.container.getItem() instanceof HoneyBucketItem) && this.getFluid().isEmpty() && this.canFillFluidType(resource)) {
            if (action.execute()) {
                this.setFluid(resource);
            }

            return 1000;
        } else {
            return 0;
        }
    }

    public FluidStack drain(FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && resource.getAmount() >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty() && FluidStack.isSameFluidSameComponents(fluidStack, resource)) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }

                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }

    public FluidStack drain(int maxDrain, IFluidHandler.FluidAction action) {
        if (this.container.getCount() == 1 && maxDrain >= 1000) {
            FluidStack fluidStack = this.getFluid();
            if (!fluidStack.isEmpty()) {
                if (action.execute()) {
                    this.setFluid(FluidStack.EMPTY);
                }

                return fluidStack;
            } else {
                return FluidStack.EMPTY;
            }
        } else {
            return FluidStack.EMPTY;
        }
    }
}
