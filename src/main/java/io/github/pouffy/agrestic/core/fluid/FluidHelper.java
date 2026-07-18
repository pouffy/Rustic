package io.github.pouffy.agrestic.core.fluid;

import com.mojang.datafixers.util.Pair;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.core.block.AgresticBlockEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.common.SoundActions;
import net.neoforged.neoforge.fluids.BaseFlowingFluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;

import javax.annotation.Nullable;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;

public class FluidHelper {
    public static final DecimalFormat COMMA_FORMAT = new DecimalFormat("#,###,###.##", DecimalFormatSymbols.getInstance(Locale.US));
    private static final String KEY_FILLED = Agrestic.makeDescriptionId("block", "tank.filled");
    private static final String KEY_DRAINED = Agrestic.makeDescriptionId("block", "tank.drained");

    public static enum FluidExchange {
        ITEM_TO_TANK, TANK_TO_ITEM;
    }

    public static boolean isWater(Fluid fluid) {
        return convertToStill(fluid) == Fluids.WATER;
    }

    public static boolean isLava(Fluid fluid) {
        return convertToStill(fluid) == Fluids.LAVA;
    }

    public static boolean isSame(FluidStack fluidStack, FluidStack fluidStack2) {
        return fluidStack.getFluid() == fluidStack2.getFluid();
    }

    public static boolean isSame(FluidStack fluidStack, Fluid fluid) {
        return fluidStack.getFluid() == fluid;
    }

    @SuppressWarnings("deprecation")
    public static boolean isTag(Fluid fluid, TagKey<Fluid> tag) {
        return fluid.is(tag);
    }

    public static boolean isTag(FluidState fluid, TagKey<Fluid> tag) {
        return fluid.is(tag);
    }

    public static boolean isTag(FluidStack fluid, TagKey<Fluid> tag) {
        return isTag(fluid.getFluid(), tag);
    }

    public static SoundEvent getFillSound(FluidStack fluid) {
        SoundEvent soundevent = fluid.getFluid()
                .getFluidType()
                .getSound(fluid, SoundActions.BUCKET_FILL);
        if (soundevent == null)
            soundevent =
                    FluidHelper.isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_FILL_LAVA : SoundEvents.BUCKET_FILL;
        return soundevent;
    }

    public static SoundEvent getEmptySound(FluidStack fluid) {
        SoundEvent soundevent = fluid.getFluid()
                .getFluidType()
                .getSound(fluid, SoundActions.BUCKET_EMPTY);
        if (soundevent == null)
            soundevent =
                    FluidHelper.isTag(fluid, FluidTags.LAVA) ? SoundEvents.BUCKET_EMPTY_LAVA : SoundEvents.BUCKET_EMPTY;
        return soundevent;
    }

    public static boolean hasBlockState(Fluid fluid) {
        BlockState blockState = fluid.defaultFluidState()
                .createLegacyBlock();
        return blockState != Blocks.AIR.defaultBlockState();
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

    public static void displayFilled(Player player, FluidStack fluidStack) {
        player.displayClientMessage(Component.translatable(KEY_FILLED, COMMA_FORMAT.format(FluidType.BUCKET_VOLUME), fluidStack.getHoverName()), true);
    }

    public static void displayDrained(Player player, FluidStack fluidStack) {
        player.displayClientMessage(Component.translatable(KEY_DRAINED, COMMA_FORMAT.format(fluidStack.getAmount()), fluidStack.getHoverName()), true);
    }

    public static boolean tryEmptyItemIntoBE(Level worldIn, Player player, InteractionHand handIn, ItemStack heldItem, AgresticBlockEntity be) {
        if (!ItemEmptying.canItemBeEmptied(worldIn, heldItem))
            return false;

        Pair<FluidStack, ItemStack> emptyingResult = ItemEmptying.emptyItem(worldIn, heldItem, true);
        IFluidHandler capability = worldIn.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);
        FluidStack fluidStack = emptyingResult.getFirst();

        if (fluidStack == null)
            return false;

        if (capability == null || fluidStack.getAmount() != capability.fill(fluidStack, IFluidHandler.FluidAction.SIMULATE))
            return false;
        if (worldIn.isClientSide)
            return true;

        ItemStack copyOfHeld = heldItem.copy();
        emptyingResult = ItemEmptying.emptyItem(worldIn, copyOfHeld, false);
        capability.fill(fluidStack, IFluidHandler.FluidAction.EXECUTE);

        if (!player.isCreative()) {
            if (copyOfHeld.isEmpty())
                player.setItemInHand(handIn, emptyingResult.getSecond());
            else {
                player.setItemInHand(handIn, copyOfHeld);
                player.getInventory()
                        .placeItemBackInInventory(emptyingResult.getSecond());
            }
        }
        return true;
    }

    public static boolean tryFillItemFromBE(Level world, Player player, InteractionHand handIn, ItemStack heldItem,
                                            AgresticBlockEntity be) {
        if (!ItemFilling.canItemBeFilled(world, heldItem))
            return false;

        IFluidHandler capability = world.getCapability(Capabilities.FluidHandler.BLOCK, be.getBlockPos(), null);

        if (capability == null)
            return false;

        for (int i = 0; i < capability.getTanks(); i++) {
            FluidStack fluid = capability.getFluidInTank(i);
            if (fluid.isEmpty())
                continue;
            int requiredAmountForItem = ItemFilling.getRequiredAmountForItem(world, heldItem, fluid.copy());
            if (requiredAmountForItem == -1)
                continue;
            if (requiredAmountForItem > fluid.getAmount())
                continue;

            if (world.isClientSide)
                return true;

            if (player.isCreative())
                heldItem = heldItem.copy();
            ItemStack out = ItemFilling.fillItem(world, requiredAmountForItem, heldItem, fluid.copy());

            FluidStack copy = fluid.copy();
            copy.setAmount(requiredAmountForItem);
            capability.drain(copy, IFluidHandler.FluidAction.EXECUTE);

            if (!player.isCreative())
                player.getInventory()
                        .placeItemBackInInventory(out);
            be.markUpdated();
            return true;
        }

        return false;
    }

    @Nullable
    public static FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidExchange preferred,
                                         int maxAmount) {
        return exchange(fluidTank, fluidItem, preferred, true, maxAmount);
    }

    @Nullable
    public static FluidExchange exchangeAll(IFluidHandler fluidTank, IFluidHandlerItem fluidItem,
                                            FluidExchange preferred) {
        return exchange(fluidTank, fluidItem, preferred, false, Integer.MAX_VALUE);
    }

    @Nullable
    private static FluidExchange exchange(IFluidHandler fluidTank, IFluidHandlerItem fluidItem, FluidExchange preferred,
                                          boolean singleOp, int maxTransferAmountPerTank) {

        // Locks in the transfer direction of this operation
        FluidExchange lockedExchange = null;

        for (int tankSlot = 0; tankSlot < fluidTank.getTanks(); tankSlot++) {
            for (int slot = 0; slot < fluidItem.getTanks(); slot++) {

                FluidStack fluidInTank = fluidTank.getFluidInTank(tankSlot);
                int tankCapacity = fluidTank.getTankCapacity(tankSlot) - fluidInTank.getAmount();
                boolean tankEmpty = fluidInTank.isEmpty();

                FluidStack fluidInItem = fluidItem.getFluidInTank(tankSlot);
                int itemCapacity = fluidItem.getTankCapacity(tankSlot) - fluidInItem.getAmount();
                boolean itemEmpty = fluidInItem.isEmpty();

                boolean undecided = lockedExchange == null;
                boolean canMoveToTank = (undecided || lockedExchange == FluidExchange.ITEM_TO_TANK) && tankCapacity > 0;
                boolean canMoveToItem = (undecided || lockedExchange == FluidExchange.TANK_TO_ITEM) && itemCapacity > 0;

                // Incompatible Liquids
                if (!tankEmpty && !itemEmpty && !FluidStack.isSameFluidSameComponents(fluidInItem, fluidInTank))
                    continue;

                // Transfer liquid to tank
                if (((tankEmpty || itemCapacity <= 0) && canMoveToTank)
                        || undecided && preferred == FluidExchange.ITEM_TO_TANK) {

                    int amount = fluidTank.fill(
                            fluidItem.drain(Math.min(maxTransferAmountPerTank, tankCapacity), IFluidHandler.FluidAction.EXECUTE),
                            IFluidHandler.FluidAction.EXECUTE);
                    if (amount > 0) {
                        lockedExchange = FluidExchange.ITEM_TO_TANK;
                        if (singleOp)
                            return lockedExchange;
                        continue;
                    }
                }

                // Transfer liquid from tank
                if (((itemEmpty || tankCapacity <= 0) && canMoveToItem)
                        || undecided && preferred == FluidExchange.TANK_TO_ITEM) {

                    int amount = fluidItem.fill(
                            fluidTank.drain(Math.min(maxTransferAmountPerTank, itemCapacity), IFluidHandler.FluidAction.EXECUTE),
                            IFluidHandler.FluidAction.EXECUTE);
                    if (amount > 0) {
                        lockedExchange = FluidExchange.TANK_TO_ITEM;
                        if (singleOp)
                            return lockedExchange;
                        continue;
                    }

                }

            }
        }

        return null;
    }
}
