package io.github.pouffy.rustic.init;

import com.pouffydev.krystal_core.foundation.data.loot.SelfBlockLootType;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockProperties;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockRegistryHelper;
import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.common.block.CrushingTubBlock;
import io.github.pouffy.rustic.common.block.LogBlock;
import io.github.pouffy.rustic.core.block.DoorBlockLootType;
import io.github.pouffy.rustic.core.block.SlabBlockLootType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class RusticBlocks {
    public static final BlockRegistryHelper HELPER = Rustic.INSTANCE.blockRegistryHelper;
    public static List<Woodset> WOODSETS = new ArrayList<>();

    public static final Woodset OLIVE = new Woodset("olive");
    public static final Woodset IRONWOOD = new Woodset("ironwood");

    public static final BlockDefinition<CrushingTubBlock> CRUSHING_TUB = HELPER.register("crushing_tub", () -> new CrushingTubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion()), BlockProperties.custom(""));

    public static void staticInit() {}

    public static class Woodset {
        private final BlockDefinition<Block> PLANKS;
        private final BlockDefinition<Block> STAIRS;
        private final BlockDefinition<Block> SLAB;
        private final BlockDefinition<Block> LOG;
        private final BlockDefinition<Block> WOOD;
        private final BlockDefinition<Block> STRIPPED_LOG;
        private final BlockDefinition<Block> STRIPPED_WOOD;
        private final BlockDefinition<FenceBlock> FENCE;
        private final BlockDefinition<FenceGateBlock> FENCE_GATE;
        private final BlockDefinition<ButtonBlock> BUTTON;
        private final BlockDefinition<PressurePlateBlock> PRESSURE_PLATE;
        private final BlockDefinition<DoorBlock> DOOR;
        private final BlockDefinition<TrapDoorBlock> TRAPDOOR;
        private final BlockDefinition<StandingSignBlock> SIGN;
        private final BlockDefinition<WallSignBlock> WALL_SIGN;
        private final BlockDefinition<CeilingHangingSignBlock> HANGING_SIGN;
        private final BlockDefinition<WallHangingSignBlock> HANGING_WALL_SIGN;

        public Woodset(String name) {
            BlockSetType blockSetType = BlockSetType.register(new BlockSetType("rustic:" + name));
            WoodType woodType = WoodType.register(new WoodType("rustic:" + name, blockSetType));
            RusticBlocks.WOODSETS.add(this);
            this.PLANKS = HELPER.register(name + "_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)), Properties.planks());
            this.STAIRS = HELPER.register(name + "_stairs", () -> new StairBlock(this.PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(this.PLANKS.get())), Properties.woodenStairs(this.PLANKS));
            this.SLAB = HELPER.register(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(this.PLANKS.get())), Properties.woodenSlab(this.PLANKS));
            this.LOG = HELPER.register(name + "_log", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)), Properties.log());
            this.WOOD = HELPER.register(name + "_wood", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)), Properties.log());
            this.STRIPPED_LOG = HELPER.register("stripped_" + name + "_log", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)), Properties.log());
            this.STRIPPED_WOOD = HELPER.register("stripped_" + name + "_wood", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)), Properties.log());
            this.FENCE = HELPER.register(name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)), Properties.woodenFence(this.PLANKS));
            this.FENCE_GATE = HELPER.register(name + "_fence_gate", () -> new FenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)), Properties.fenceGate(this.PLANKS));
            this.BUTTON = HELPER.register(name + "_button", () -> new ButtonBlock(blockSetType, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), Properties.woodenButton(this.PLANKS));
            this.PRESSURE_PLATE = HELPER.register(name + "_pressure_plate", () -> new PressurePlateBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)), Properties.woodenPressurePlate(this.PLANKS));
            this.DOOR = HELPER.register(name + "_door", () -> new DoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), Properties.woodenDoor(this.PLANKS));
            this.TRAPDOOR = HELPER.register(name + "_trapdoor", () -> new TrapDoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)), Properties.woodenTrapdoor(this.PLANKS));
            this.SIGN = HELPER.registerNoItem(name + "_sign", () -> new StandingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)), Properties.sign());
            this.WALL_SIGN = HELPER.registerNoItem(name + "_wall_sign", () -> {
                Block signBlock = this.SIGN.get();
                return new WallSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(() -> signBlock));
            }, BlockProperties.custom(null));
            this.HANGING_SIGN = HELPER.registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)), Properties.hangingSign());
            this.HANGING_WALL_SIGN = HELPER.registerNoItem(name + "_wall_hanging_sign", () -> {
                Block hangingSignBlock = this.HANGING_SIGN.get();
                return new WallHangingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_HANGING_SIGN).lootFrom(() -> hangingSignBlock));
            }, BlockProperties.custom(null));
        }

        public BlockDefinition<Block> planks() {
            return this.PLANKS;
        }

        public BlockDefinition<Block> stairs() {
            return this.STAIRS;
        }

        public BlockDefinition<Block> slab() {
            return this.SLAB;
        }

        public BlockDefinition<Block> log() {
            return this.LOG;
        }

        public BlockDefinition<Block> wood() {
            return this.WOOD;
        }

        public BlockDefinition<Block> strippedLog() {
            return this.STRIPPED_LOG;
        }

        public BlockDefinition<Block> strippedWood() {
            return this.STRIPPED_WOOD;
        }

        public BlockDefinition<FenceBlock> fence() {
            return this.FENCE;
        }

        public BlockDefinition<FenceGateBlock> fenceGate() {
            return this.FENCE_GATE;
        }

        public BlockDefinition<ButtonBlock> button() {
            return this.BUTTON;
        }

        public BlockDefinition<PressurePlateBlock> pressurePlate() {
            return this.PRESSURE_PLATE;
        }

        public BlockDefinition<DoorBlock> door() {
            return this.DOOR;
        }

        public BlockDefinition<TrapDoorBlock> trapdoor() {
            return this.TRAPDOOR;
        }

        public BlockDefinition<StandingSignBlock> sign() {
            return this.SIGN;
        }

        public BlockDefinition<WallSignBlock> wallSign() {
            return this.WALL_SIGN;
        }

        public BlockDefinition<CeilingHangingSignBlock> hangingSign() {
            return this.HANGING_SIGN;
        }

        public BlockDefinition<WallHangingSignBlock> hangingWallSign() {
            return this.HANGING_WALL_SIGN;
        }

        public void setFlammables() {
            FireBlock fireBlock = (FireBlock)Blocks.FIRE;
            fireBlock.setFlammable(this.PLANKS.block(), 5, 20);
            fireBlock.setFlammable(this.STAIRS.block(), 5, 20);
            fireBlock.setFlammable(this.SLAB.block(), 5, 20);
            fireBlock.setFlammable(this.FENCE.block(), 5, 20);
        }

        public @Nullable BlockState checkLogStripping(BlockState state) {
            if (state.is(this.LOG.block())) {
                return this.STRIPPED_LOG.block().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS));
            } else {
                return state.is(this.WOOD.block()) ? this.STRIPPED_WOOD.block().defaultBlockState().setValue(RotatedPillarBlock.AXIS, state.getValue(RotatedPillarBlock.AXIS)) : null;
            }
        }
    }

    static class Properties {
        public static BlockProperties log() {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties planks() {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties woodenButton(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties woodenDoor(BlockDefinition<?> planks) {
            return new BlockProperties(new DoorBlockLootType(), "");
        }

        public static BlockProperties woodenFence(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties fenceGate(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties woodenPressurePlate(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties woodenSlab(BlockDefinition<?> planks) {
            return new BlockProperties(new SlabBlockLootType(), "");
        }

        public static BlockProperties woodenStairs(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties woodenTrapdoor(BlockDefinition<?> planks) {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties sign() {
            return new BlockProperties(new SelfBlockLootType(), "");
        }

        public static BlockProperties hangingSign() {
            return new BlockProperties(new SelfBlockLootType(), "");
        }
    }
}
