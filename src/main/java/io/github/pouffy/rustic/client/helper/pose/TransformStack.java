package io.github.pouffy.rustic.client.helper.pose;

import com.mojang.blaze3d.vertex.PoseStack;

public interface TransformStack<Self extends TransformStack<Self>> extends Transform<Self> {
    static PoseTransformStack of(PoseStack stack) {
        return ((PoseStackExtension) stack).flywheel$transformStack();
    }

    Self pushPose();

    Self popPose();
}
