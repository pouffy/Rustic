package io.github.pouffy.rustic.datagen.client;

import io.github.pouffy.rustic.Rustic;
import io.github.pouffy.rustic.init.RusticBlocks;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Rustic.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        for (RusticBlocks.Woodset woodset : RusticBlocks.WOODSETS) {
            String name = woodset.planks().getId().getPath().replace("_planks", "");
            simpleBlockAndItem(woodset.planks(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            simpleSlab(woodset.slab(), woodset.planks(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            simpleStairs(woodset.stairs(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            cubeColumnAxis(woodset.log(), Rustic.location("block/wooden/%s/log".formatted(name)));
            cubeColumnAxisSingle(woodset.wood(), Rustic.location("block/wooden/%s/log".formatted(name)));
            cubeColumnAxis(woodset.strippedLog(), Rustic.location("block/wooden/%s/stripped_log".formatted(name)));
            cubeColumnAxisSingle(woodset.strippedWood(), Rustic.location("block/wooden/%s/stripped_log".formatted(name)));
            simpleFence(woodset.fence(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            simpleFenceGate(woodset.fenceGate(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            buttonBlock(woodset.button().get(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            pressurePlateBlock(woodset.pressurePlate().get(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            altDoorBlock(woodset.door().get(), Rustic.location("block/wooden/%s/door".formatted(name)));
            altTrapdoorBlock(woodset.trapdoor().get(), Rustic.location("block/wooden/%s/trapdoor".formatted(name)));
            justParticle(woodset.sign(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            justParticle(woodset.wallSign(), Rustic.location("block/wooden/%s/planks".formatted(name)));
            justParticle(woodset.hangingSign(), Rustic.location("block/wooden/%s/stripped_log".formatted(name)));
            justParticle(woodset.hangingWallSign(), Rustic.location("block/wooden/%s/stripped_log".formatted(name)));
        }
        simpleExisting(RusticBlocks.CRUSHING_TUB, Rustic.location("block/crushing_tub"));
    }

    private void simpleBlockItem(Supplier<? extends Block> block) {
        super.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockWithItem(Supplier<? extends Block> block) {
        super.simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block) {
        this.simpleBlock(block.get());
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, RenderType renderType) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), this.blockTexture(block.get())).renderType(renderType.name));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, ResourceLocation texture) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, ResourceLocation texture, RenderType renderType) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), texture).renderType(renderType.name));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void cubeColumn(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.simpleBlock(block.get(), this.models().cubeColumn(name, texture, texture.withSuffix("_end")));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }

    private void cubeColumnAxis(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        var vertical = models().cubeColumn(name, texture, texture.withSuffix("_end"));
        var horizontal = models().cubeColumnHorizontal(name + "_horizontal", texture, texture.withSuffix("_end"));
        getVariantBuilder(block.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(vertical).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(horizontal).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(horizontal).rotationX(90).rotationY(90).addModel();
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }

    private void cubeColumnAxisSingle(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        var model = models().cubeColumn(name, texture, texture);
        getVariantBuilder(block.get())
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Y)
                .modelForState().modelFile(model).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.Z)
                .modelForState().modelFile(model).rotationX(90).addModel()
                .partialState().with(RotatedPillarBlock.AXIS, Direction.Axis.X)
                .modelForState().modelFile(model).rotationX(90).rotationY(90).addModel();
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }

    private void cubePillar(Supplier<? extends Block> block, ResourceLocation end) {
        String name = this.name(block.get());
        ResourceLocation side = Rustic.location("block/" + name + "/side");
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, end, end));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void cubeBottomTop(Supplier<? extends Block> block, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation side = Rustic.location("block/" + name);
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void blockBottomTop(Supplier<? extends Block> block) {
        String name = this.name(block.get());
        ResourceLocation side = Rustic.location("block/" + name);
        ResourceLocation top = Rustic.location("block/" + name + "_top");
        ResourceLocation bottom = Rustic.location("block/" + name + "_bottom");
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()))));
    }
    private void sidedSlab(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation doubleSlab = Rustic.location("block/" + name.replace("_slab", ""));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, side, top, bottom);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void sidedStairs(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        this.stairsBlock((StairBlock) block.get(), name, side, top, bottom);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }

    private void simpleSlab(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        ResourceLocation doubleSlab = Rustic.location("block/" + this.name(texture.get()));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, main, main, main);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleStairs(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        this.stairsBlock((StairBlock) block.get(), name, main, main, main);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleSlab(Supplier<? extends Block> block, Supplier<? extends Block> parent, ResourceLocation texture) {
        String name = this.name(block.get());
        ResourceLocation doubleSlab = Rustic.location("block/" + this.name(parent.get()));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, texture, texture, texture);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", texture, texture, texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleStairs(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.stairsBlock((StairBlock) block.get(), name, texture, texture, texture);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", texture, texture, texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleExisting(Supplier<? extends Block> block, ResourceLocation existing) {
        this.simpleBlock(block.get(), new ModelFile.UncheckedModelFile(existing));
        this.simpleBlockItem(block);
    }

    private void horizontalFacing(Supplier<? extends Block> block, String loc) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            ResourceLocation modelLoc = Rustic.location("block/" + loc);
            int yRot = Mth.floor((state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360);
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modelLoc)).rotationY(yRot).build();
        });
    }

    public void buttonBlock(ButtonBlock block, ResourceLocation texture) {
        ModelFile button = this.models().button(this.name(block), texture);
        ModelFile buttonPressed = this.models().buttonPressed(this.name(block) + "_pressed", texture);
        this.buttonBlock(block, button, buttonPressed);
        this.models().withExistingParent(this.name(block) + "_inventory", "block/button_inventory").texture("texture", texture);
        this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block) + "_inventory")));
    }

    public void pressurePlateBlock(PressurePlateBlock block, ResourceLocation texture) {
        ModelFile pressurePlate = this.models().pressurePlate(this.name(block), texture);
        ModelFile pressurePlateDown = this.models().pressurePlateDown(this.name(block) + "_down", texture);
        this.pressurePlateBlock(block, pressurePlate, pressurePlateDown);
        this.simpleBlockItem(block, pressurePlate);
    }

    private void simpleFence(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.fourWayBlock((FenceBlock) block.get(), this.models().fencePost(name + "_post", texture), this.models().fenceSide(name + "_side", texture));
        this.simpleBlockItem(block.get(), models().fenceInventory(name + "_inventory", texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleFenceGate(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.fenceGateBlock((FenceGateBlock) block.get(), name.replace("_fence_gate", ""), texture);
        this.simpleBlockItem(block.get(), models().fenceGate(name + "_inventory", texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block.get()) + "_inventory")));
    }

    private void altDoorBlock(DoorBlock block, ResourceLocation texture) {
        String name = this.name(block);
        ResourceLocation bottom = texture.withSuffix("_bottom");
        ResourceLocation top = texture.withSuffix("_top");
        ResourceLocation sides = texture.withSuffix("_sides");
        ModelFile bottomLeft = this.models().withExistingParent(name + "_bottom_left", Rustic.location("block/template/alt_door_bottom_left")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile bottomLeftOpen = this.models().withExistingParent(name + "_bottom_left_open", Rustic.location("block/template/alt_door_bottom_left_open")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile bottomRight = this.models().withExistingParent(name + "_bottom_right", Rustic.location("block/template/alt_door_bottom_right")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile bottomRightOpen = this.models().withExistingParent(name + "_bottom_right_open", Rustic.location("block/template/alt_door_bottom_right_open")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile topLeft = this.models().withExistingParent(name + "_top_left", Rustic.location("block/template/alt_door_top_left")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile topLeftOpen = this.models().withExistingParent(name + "_top_left_open", Rustic.location("block/template/alt_door_top_left_open")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile topRight = this.models().withExistingParent(name + "_top_right", Rustic.location("block/template/alt_door_top_right")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        ModelFile topRightOpen = this.models().withExistingParent(name + "_top_right_open", Rustic.location("block/template/alt_door_top_right_open")).texture("bottom", bottom).texture("top", top).texture("side", sides);
        this.doorBlock(block, bottomLeft, bottomLeftOpen, bottomRight, bottomRightOpen, topLeft, topLeftOpen, topRight, topRightOpen);
    }

    private void altTrapdoorBlock(TrapDoorBlock block, ResourceLocation texture) {
        String name = this.name(block);
        ModelFile bottom = this.models().withExistingParent(name + "_bottom", Rustic.location("block/template/alt_trapdoor_bottom")).texture("front", texture).texture("side", texture.withSuffix("_sides"));
        ModelFile top = this.models().withExistingParent(name + "_top", Rustic.location("block/template/alt_trapdoor_top")).texture("front", texture).texture("side", texture.withSuffix("_sides"));
        ModelFile open = this.models().withExistingParent(name + "_open", Rustic.location("block/template/alt_trapdoor_open")).texture("front", texture).texture("side", texture.withSuffix("_sides"));
        this.trapdoorBlock(block, bottom, top, open, true);
        this.simpleBlockItem(block, new ModelFile.UncheckedModelFile(Rustic.location("block/" + this.name(block) + "_bottom")));
    }

    private void justParticle(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.simpleBlock(block.get(), models().getBuilder(name).texture("particle", texture));
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);}

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
