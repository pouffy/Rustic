package io.github.pouffy.agrestic.init;

import com.pouffydev.krystal_core.foundation.data.loot.SelfBlockLootType;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockDefinition;
import com.pouffydev.krystal_core.foundation.registry.definition.block.BlockProperties;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.common.block.CrushingTubBlock;
import io.github.pouffy.agrestic.common.block.HerbBlock;
import io.github.pouffy.agrestic.common.block.LogBlock;
import io.github.pouffy.agrestic.core.block.DoorBlockLootType;
import io.github.pouffy.agrestic.core.block.ILightEmitting;
import io.github.pouffy.agrestic.core.block.SlabBlockLootType;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.WoodType;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class AgresticBlocks {
    public static final DeferredRegister.Blocks HELPER = DeferredRegister.createBlocks(Agrestic.MODID);
    public static List<Woodset> WOODSETS = new ArrayList<>();
    public static List<BlockDefinition<?>> BLOCK_DEFINITIONS = new ArrayList<>();

    public static final Woodset OLIVE = new Woodset("olive");
    public static final Woodset IRONWOOD = new Woodset("ironwood");

    public static final BlockDefinition<CrushingTubBlock> CRUSHING_TUB = register("crushing_tub", () -> new CrushingTubBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS).noOcclusion().lightLevel(ILightEmitting.LIGHT_GETTER)));

    public static final BlockDefinition<HerbBlock> ALOE_VERA = register("aloe_vera", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> BLOOD_ORCHID = register("blood_orchid", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> CHAMOMILE = register("chamomile", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> CLOUDSBLUFF = registerNoItem("cloudsbluff", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return AgresticItems.CLOUDSBLUFF;
        }
    });
    public static final BlockDefinition<HerbBlock> COHOSH = register("cohosh", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> CORE_ROOT = registerNoItem("core_root", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return AgresticItems.CORE_ROOT;
        }
    });
    public static final BlockDefinition<HerbBlock> DEATHSTALK = register("deathstalk", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> GINSENG = registerNoItem("ginseng", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return AgresticItems.GINSENG;
        }
    });
    public static final BlockDefinition<HerbBlock> HORSETAIL = register("horsetail", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> MARSH_MALLOW = registerNoItem("marsh_mallow", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return AgresticItems.MARSH_MALLOW;
        }
    });
    public static final BlockDefinition<HerbBlock> MOONCAP = register("moonscap", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
        @Override
        public int getMinLight() {
            return 8;
        }
    });
    public static final BlockDefinition<HerbBlock> VANTA_LILY = register("vanta_lily", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });
    public static final BlockDefinition<HerbBlock> WIND_THISTLE = register("wind_thistle", () -> new HerbBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.SHORT_GRASS)) {
        @Override
        public @NotNull Supplier<? extends Item> getHerb() {
            return this::asItem;
        }
    });

    public static void staticInit(IEventBus bus) {
        HELPER.register(bus);
    }

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
            BlockSetType blockSetType = BlockSetType.register(new BlockSetType("agrestic:" + name));
            WoodType woodType = WoodType.register(new WoodType("agrestic:" + name, blockSetType));
            AgresticBlocks.WOODSETS.add(this);
            this.PLANKS = register(name + "_planks", () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PLANKS)), Properties.planks());
            this.STAIRS = register(name + "_stairs", () -> new StairBlock(this.PLANKS.get().defaultBlockState(), BlockBehaviour.Properties.ofFullCopy(this.PLANKS.get())), Properties.woodenStairs(this.PLANKS));
            this.SLAB = register(name + "_slab", () -> new SlabBlock(BlockBehaviour.Properties.ofFullCopy(this.PLANKS.get())), Properties.woodenSlab(this.PLANKS));
            this.LOG = register(name + "_log", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_LOG)), Properties.log());
            this.WOOD = register(name + "_wood", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WOOD)), Properties.log());
            this.STRIPPED_LOG = register("stripped_" + name + "_log", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_LOG)), Properties.log());
            this.STRIPPED_WOOD = register("stripped_" + name + "_wood", () -> new LogBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.STRIPPED_OAK_WOOD)), Properties.log());
            this.FENCE = register(name + "_fence", () -> new FenceBlock(BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE)), Properties.woodenFence(this.PLANKS));
            this.FENCE_GATE = register(name + "_fence_gate", () -> new FenceGateBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_FENCE_GATE)), Properties.fenceGate(this.PLANKS));
            this.BUTTON = register(name + "_button", () -> new ButtonBlock(blockSetType, 15, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_BUTTON)), Properties.woodenButton(this.PLANKS));
            this.PRESSURE_PLATE = register(name + "_pressure_plate", () -> new PressurePlateBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_PRESSURE_PLATE)), Properties.woodenPressurePlate(this.PLANKS));
            this.DOOR = register(name + "_door", () -> new DoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_DOOR)), Properties.woodenDoor(this.PLANKS));
            this.TRAPDOOR = register(name + "_trapdoor", () -> new TrapDoorBlock(blockSetType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_TRAPDOOR)), Properties.woodenTrapdoor(this.PLANKS));
            this.SIGN = registerNoItem(name + "_sign", () -> new StandingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_SIGN)), Properties.sign());
            this.WALL_SIGN = registerNoItem(name + "_wall_sign", () -> {
                Block signBlock = this.SIGN.get();
                return new WallSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_WALL_SIGN).lootFrom(() -> signBlock));
            }, BlockProperties.custom(null));
            this.HANGING_SIGN = registerNoItem(name + "_hanging_sign", () -> new CeilingHangingSignBlock(woodType, BlockBehaviour.Properties.ofFullCopy(Blocks.OAK_HANGING_SIGN)), Properties.hangingSign());
            this.HANGING_WALL_SIGN = registerNoItem(name + "_wall_hanging_sign", () -> {
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

    public static <T extends Block> BlockDefinition<T> registerNoItem(String name, Supplier<T> block, BlockProperties properties) {
        DeferredBlock<T> deferred = HELPER.register(name, block);
        BlockDefinition<T> definition = BlockDefinition.fromHolder(deferred, properties);
        BLOCK_DEFINITIONS.add(definition);
        return definition;
    }

    public static <T extends Block> BlockDefinition<T> registerNoItem(String name, Supplier<T> block) {
        return registerNoItem(name, block, BlockProperties.custom(""));
    }

    public static <T extends Block> BlockDefinition<T> register(String name, Supplier<T> block, BlockProperties properties) {
        BlockDefinition<T> definition = registerNoItem(name, block, properties);
        AgresticItems.register(name, () -> new BlockItem(definition.get(), new Item.Properties()));
        return definition;
    }

    public static <T extends Block> BlockDefinition<T> register(String name, Supplier<T> block) {
        return register(name, block, BlockProperties.custom(""));
    }

}
