package io.github.pouffy.agrestic.datagen.server.tags;

import com.pouffydev.krystal_core.datagen.server.KCDamageTagsProvider;
import io.github.pouffy.agrestic.datagen.server.bootstrap.AgresticDamageTypes;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.DamageTypeTagsProvider;
import net.minecraft.tags.DamageTypeTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModDamageTagsProvider extends DamageTypeTagsProvider {

    public ModDamageTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, String modId, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, modId, existingFileHelper);
    }

    protected void addTags(HolderLookup.Provider lookupProvider) {
        tag(Tags.DamageTypes.IS_MAGIC).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_ARMOR).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_ENCHANTMENTS).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_RESISTANCE).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_EFFECTS).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_COOLDOWN).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.BYPASSES_INVULNERABILITY).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.NO_IMPACT).add(AgresticDamageTypes.BAD_AMBROSIA);
        tag(DamageTypeTags.NO_KNOCKBACK).add(AgresticDamageTypes.BAD_AMBROSIA);
    }
}
