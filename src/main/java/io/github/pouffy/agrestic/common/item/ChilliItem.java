package io.github.pouffy.agrestic.common.item;

import io.github.pouffy.agrestic.core.item.AgresticFoodItem;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ChilliItem extends AgresticFoodItem {
    private final boolean ghost;

    public ChilliItem(Properties properties, boolean ghost) {
        super(properties);
        this.ghost = ghost;
    }

    @Override
    public ItemStack finishUsingItem(ItemStack stack, Level world, LivingEntity user) {
        if (!world.isClientSide()) {
            boolean valid = ghost || world.getRandom().nextInt(24) == 0;
            if (valid) {
                user.hurt(world.damageSources().onFire(), ghost ? 2.0F : 1.0F);
            }
        }
        return super.finishUsingItem(stack, world, user);
    }
}
