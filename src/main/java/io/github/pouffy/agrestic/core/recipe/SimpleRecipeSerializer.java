package io.github.pouffy.agrestic.core.recipe;

import com.mojang.serialization.MapCodec;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeSerializer;

public class SimpleRecipeSerializer<T extends Recipe<?>> implements RecipeSerializer<T> {
    public final MapCodec<T> codec;
    public final StreamCodec<RegistryFriendlyByteBuf, T> streamCodec;

    public SimpleRecipeSerializer(MapCodec<T> codec) {
        this.codec = codec;
        this.streamCodec = ByteBufCodecs.fromCodecWithRegistries(codec.codec());
    }

    @Override
    public MapCodec<T> codec() {
        return codec;
    }

    @Override
    public StreamCodec<RegistryFriendlyByteBuf, T> streamCodec() {
        return streamCodec;
    }
}
