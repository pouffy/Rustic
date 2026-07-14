package io.github.pouffy.agrestic.init;

import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FireBlock;

public class AgresticFlammables {

    public static void register() {
        FireBlock fireBlock = (FireBlock) Blocks.FIRE;

        AgresticBlocks.OLIVE.setFlammables();

        AgresticBlocks.IRONWOOD.setFlammables();

        fireBlock.setFlammable(AgresticBlocks.BLOOD_ORCHID.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.CHAMOMILE.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.CLOUDSBLUFF.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.COHOSH.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.CORE_ROOT.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.GINSENG.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.HORSETAIL.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.MARSH_MALLOW.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.WIND_THISTLE.get(), 60, 100);
        fireBlock.setFlammable(AgresticBlocks.VANTA_LILY.get(), 60, 100);

    }
}
