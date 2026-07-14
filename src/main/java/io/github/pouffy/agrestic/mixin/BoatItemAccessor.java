package io.github.pouffy.agrestic.mixin;

import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.BoatItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(BoatItem.class)
public interface BoatItemAccessor {

    @Accessor("type")
    Boat.Type type();

    @Accessor("hasChest")
    boolean hasChest();
}
