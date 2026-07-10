package io.github.pouffy.rustic.client.helper;

import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class VecHelper {
    public static final Vec3 CENTER_OF_ORIGIN = new Vec3(.5, .5, .5);

    public static Vec3 getCenterOf(Vec3i pos) {
        if (pos.equals(Vec3i.ZERO))
            return CENTER_OF_ORIGIN;
        return Vec3.atLowerCornerOf(pos)
                .add(.5f, .5f, .5f);
    }
}
