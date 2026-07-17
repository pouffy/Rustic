package io.github.pouffy.agrestic.common.data;

import com.mojang.datafixers.types.Func;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.core.data.BlockStateConditionSet;
import io.netty.buffer.ByteBuf;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.common.conditions.ConditionalOps;
import net.neoforged.neoforge.common.conditions.WithConditions;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

public record EvaporationBooster(HolderSet<Block> owners, Optional<BlockStateConditionSet> stateConditions, float multiplier) {

    public static final Codec<EvaporationBooster> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.BLOCK).fieldOf("owners").forGetter(EvaporationBooster::owners),
            BlockStateConditionSet.CODEC.optionalFieldOf("state_conditions").forGetter(EvaporationBooster::stateConditions),
            Codec.FLOAT.fieldOf("multiplier").forGetter(EvaporationBooster::multiplier)
    ).apply(instance, EvaporationBooster::new));
    public static final Codec<Optional<WithConditions<EvaporationBooster>>> CONDITIONAL_CODEC = ConditionalOps.createConditionalCodecWithConditions(CODEC);

    public static final StreamCodec<ByteBuf, EvaporationBooster> STREAM_CODEC = ByteBufCodecs.fromCodec(CODEC);

    public boolean test(BlockState state) {
        if (state.is(owners)) {
            return stateConditions.map((set) -> set.matches(state.getBlock().getStateDefinition(), state)).orElse(true);
        }
        return false;
    }

    public static Builder builder(float multiplier, HolderSet<Block> owners) {
        return new Builder(multiplier, owners);
    }

    public static Builder builder(float multiplier, TagKey<Block> owners) {
        return new Builder(multiplier, owners);
    }

    public static Builder builder(float multiplier, Block... owners) {
        return new Builder(multiplier, owners);
    }

    public static class Builder {
        private final HolderSet<Block> owners;
        private final float multiplier;
        private BlockStateConditionSet.Builder conditionBuilder;

        public Builder(float multiplier, HolderSet<Block> owners) {
            this.owners = owners;
            this.multiplier = multiplier;
        }

        public Builder(float multiplier, TagKey<Block> owners) {
            this(multiplier, BuiltInRegistries.BLOCK.getOrCreateTag(owners));
        }

        public Builder(float multiplier, Block... owners) {
            this(multiplier, HolderSet.direct(Arrays.stream(owners).map(BuiltInRegistries.BLOCK::wrapAsHolder).toList()));
        }

        public Builder conditions(Function<BlockStateConditionSet.Builder, BlockStateConditionSet.Builder> conditionFunction) {
            this.conditionBuilder = conditionFunction.apply(new BlockStateConditionSet.Builder());
            return this;
        }

        public EvaporationBooster build() {
            return new EvaporationBooster(owners, Optional.ofNullable(conditionBuilder).map(BlockStateConditionSet.Builder::build), multiplier);
        }
    }
}
