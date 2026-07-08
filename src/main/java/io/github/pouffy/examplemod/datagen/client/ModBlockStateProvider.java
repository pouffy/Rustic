package io.github.pouffy.examplemod.datagen.client;

import io.github.pouffy.examplemod.Example;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.generators.BlockStateProvider;
import net.neoforged.neoforge.client.model.generators.ConfiguredModel;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModBlockStateProvider extends BlockStateProvider {

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, Example.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

    }

    private void simpleBlockItem(Supplier<? extends Block> block) {
        super.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockWithItem(Supplier<? extends Block> block) {
        super.simpleBlockWithItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block) {
        this.simpleBlock(block.get());
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, RenderType renderType) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), this.blockTexture(block.get())).renderType(renderType.name));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, ResourceLocation texture) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), texture));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void simpleBlockAndItem(Supplier<? extends Block> block, ResourceLocation texture, RenderType renderType) {
        this.simpleBlock(block.get(), this.models().cubeAll(this.name(block.get()), texture).renderType(renderType.name));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void cubeColumn(Supplier<? extends Block> block, ResourceLocation texture) {
        String name = this.name(block.get());
        this.simpleBlock(block.get(), this.models().cubeColumn(name, texture, texture.withSuffix("_end")));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
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
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void cubePillar(Supplier<? extends Block> block, ResourceLocation end) {
        String name = this.name(block.get());
        ResourceLocation side = Example.location("block/" + name + "/side");
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, end, end));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void cubeBottomTop(Supplier<? extends Block> block, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation side = Example.location("block/" + name);
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void blockBottomTop(Supplier<? extends Block> block) {
        String name = this.name(block.get());
        ResourceLocation side = Example.location("block/" + name);
        ResourceLocation top = Example.location("block/" + name + "_top");
        ResourceLocation bottom = Example.location("block/" + name + "_bottom");
        this.simpleBlock(block.get(), this.models().cubeBottomTop(name, side, bottom, top));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()))));
    }
    private void sidedSlab(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        ResourceLocation doubleSlab = Example.location("block/" + name.replace("_slab", ""));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, side, top, bottom);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void sidedStairs(Supplier<? extends Block> block, ResourceLocation side, ResourceLocation top, ResourceLocation bottom) {
        String name = this.name(block.get());
        this.stairsBlock((StairBlock) block.get(), name, side, top, bottom);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", side, top, bottom));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()) + "_inventory")));
    }

    private void simpleSlab(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        ResourceLocation doubleSlab = Example.location("block/" + this.name(texture.get()));
        this.slabBlock((SlabBlock) block.get(), doubleSlab, main, main, main);
        this.simpleBlockItem(block.get(), models().slab(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleStairs(Supplier<? extends Block> block, Supplier<? extends Block> texture) {
        String name = this.name(block.get());
        ResourceLocation main = blockTexture(texture.get());
        this.stairsBlock((StairBlock) block.get(), name, main, main, main);
        this.simpleBlockItem(block.get(), models().stairs(name + "_inventory", main, main, main));
        this.simpleBlockItem(block.get(), new ModelFile.UncheckedModelFile(Example.location("block/" + this.name(block.get()) + "_inventory")));
    }
    private void simpleExisting(Supplier<? extends Block> block, ResourceLocation existing) {
        this.simpleBlock(block.get(), new ModelFile.UncheckedModelFile(existing));
        this.simpleBlockItem(block);
    }

    private void horizontalFacing(Supplier<? extends Block> block, String loc) {
        getVariantBuilder(block.get()).forAllStatesExcept(state -> {
            ResourceLocation modelLoc = Example.location("block/" + loc);
            int yRot = Mth.floor((state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180) % 360);
            return ConfiguredModel.builder().modelFile(new ModelFile.UncheckedModelFile(modelLoc)).rotationY(yRot).build();
        });
    }

    private ResourceLocation extend(ResourceLocation rl, String suffix) {return ResourceLocation.fromNamespaceAndPath(rl.getNamespace(), rl.getPath() + suffix);}

    private String name(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
