package io.github.pouffy.agrestic.datagen.client;

import com.pouffydev.krystal_core.foundation.data.provider.client.KrystalItemModelProvider;
import io.github.pouffy.agrestic.Agrestic;
import io.github.pouffy.agrestic.init.AgresticBlocks;
import io.github.pouffy.agrestic.init.AgresticItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.function.Supplier;

public class ModItemModelProvider extends KrystalItemModelProvider {
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Agrestic.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (AgresticBlocks.Woodset woodset : AgresticBlocks.WOODSETS) {
            String name = woodset.planks().getId().getPath().replace("_planks", "");
            this.basicItem(woodset.door()::asItem, "wooden/%s/door".formatted(name));
        }
        this.basicItem(AgresticItems.OLIVE_SIGN, "wooden/olive/sign");
        this.basicItem(AgresticItems.OLIVE_HANGING_SIGN, "wooden/olive/hanging_sign");
        this.basicItem(AgresticItems.OLIVE_BOAT, "wooden/olive/boat");
        this.basicItem(AgresticItems.OLIVE_CHEST_BOAT, "wooden/olive/boat_with_chest");
        this.basicItem(AgresticItems.IRONWOOD_SIGN, "wooden/ironwood/sign");
        this.basicItem(AgresticItems.IRONWOOD_HANGING_SIGN, "wooden/ironwood/hanging_sign");
        this.basicItem(AgresticItems.IRONWOOD_BOAT, "wooden/ironwood/boat");
        this.basicItem(AgresticItems.IRONWOOD_CHEST_BOAT, "wooden/ironwood/boat_with_chest");

        this.blockItem(AgresticBlocks.IRONWOOD_SAPLING::asItem, "wooden/ironwood/sapling");
        this.blockItem(AgresticBlocks.OLIVE_SAPLING::asItem, "wooden/olive/sapling");

        this.basicItem(AgresticItems.IRONBERRIES);
        this.basicItem(AgresticItems.OLIVES);
        this.basicItem(AgresticItems.TOMATO);
        this.basicItem(AgresticItems.CHILLI_PEPPER);
        this.basicItem(AgresticItems.GHOST_PEPPER);
        this.basicItem(AgresticItems.CHILLI_PEPPER_SEEDS);
        this.basicItem(AgresticItems.GRAPES);
        this.basicItem(AgresticItems.TALLOW);
        this.basicItem(AgresticItems.GOLD_DUST);
        this.basicItem(AgresticItems.TINY_GOLD_DUST);
        this.basicItem(AgresticItems.IRON_DUST);
        this.basicItem(AgresticItems.TINY_IRON_DUST);

        this.bucket(AgresticItems.ALE_WORT_BUCKET);

        this.bottle(AgresticItems.APPLE_JUICE_BOTTLE);
        this.bottle(AgresticItems.GOLDEN_APPLE_JUICE_BOTTLE);
        this.bottle(AgresticItems.GRAPE_JUICE_BOTTLE);
        this.bottle(AgresticItems.SWEET_BERRY_JUICE_BOTTLE);
        this.bottle(AgresticItems.IRONBERRY_JUICE_BOTTLE);
        this.bottle(AgresticItems.ALE_WORT_BOTTLE);
        this.bottle(AgresticItems.OLIVE_OIL_BOTTLE);
        this.bottle(AgresticItems.VANTA_OIL_BOTTLE);

        this.herb(AgresticBlocks.ALOE_VERA::asItem);
        this.herb(AgresticBlocks.BLOOD_ORCHID::asItem);
        this.herb(AgresticBlocks.CHAMOMILE::asItem);
        this.herb(AgresticBlocks.CLOUDSBLUFF::asItem);
        this.herb(AgresticBlocks.COHOSH::asItem);
        this.herb(AgresticBlocks.CORE_ROOT::asItem);
        this.blockItem(AgresticBlocks.DEATHSTALK::asItem, "herbs/deathstalk_6");
        this.herb(AgresticBlocks.GINSENG::asItem);
        this.herb(AgresticBlocks.HORSETAIL::asItem);
        this.herb(AgresticBlocks.MARSH_MALLOW::asItem);
        this.blockItem(AgresticBlocks.MOONCAP::asItem, "herbs/mooncap_6");
        this.herb(AgresticBlocks.VANTA_LILY::asItem);
        this.herb(AgresticBlocks.WIND_THISTLE::asItem);
    }

    public ItemModelBuilder blockItem(Supplier<? extends Item> item, String texture) {
        return this.getBuilder(itemName(item.get())).parent(new ModelFile.UncheckedModelFile("item/generated")).texture("layer0", ResourceLocation.fromNamespaceAndPath(this.modid, "block/%s".formatted(texture)));
    }

    public ItemModelBuilder herb(Supplier<? extends Item> item) {
        return this.basicItem(item, "herbs/" + itemName(item.get()), "");
    }

    public ItemModelBuilder bucket(Supplier<? extends Item> item) {
        return this.basicItem(item, "bucket/" + itemName(item.get()).replace("_bucket", ""), "");
    }

    public ItemModelBuilder bottle(Supplier<? extends Item> item) {
        return this.basicItem(item, "bottle/" + itemName(item.get()).replace("_bottle", ""), "");
    }
}
