package io.github.pouffy.agrestic.core.recipe;

import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.pouffy.agrestic.init.AgresticIngredientTypes;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponentPredicate;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.neoforge.common.crafting.ICustomIngredient;
import net.neoforged.neoforge.common.crafting.IngredientType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

public record ComponentIngredient(List<Ingredient> children, DataComponentPredicate components, List<Holder<DataComponentType<?>>> expectedTypes) implements ICustomIngredient {

    public static final MapCodec<ComponentIngredient> CODEC = RecordCodecBuilder.mapCodec((instance) -> instance.group(
            Ingredient.LIST_CODEC_NONEMPTY.fieldOf("children").forGetter(ComponentIngredient::children),
            DataComponentPredicate.CODEC.fieldOf("components").forGetter(ComponentIngredient::components),
            BuiltInRegistries.DATA_COMPONENT_TYPE.holderByNameCodec().listOf().optionalFieldOf("expected_types", List.of()).forGetter(ComponentIngredient::expectedTypes)
    ).apply(instance, ComponentIngredient::new));

    public static Ingredient of(DataComponentPredicate predicate, Ingredient... children) {
        if (children.length == 0) {
            return Ingredient.EMPTY;
        } else {
            return new ComponentIngredient(List.of(children), predicate, List.of()).toVanilla();
        }
    }

    public static Ingredient of(DataComponentPredicate predicate, List<Holder<DataComponentType<?>>> expectedTypes, Ingredient... children) {
        if (children.length == 0) {
            return Ingredient.EMPTY;
        } else {
            return children.length == 1 ? children[0] : (new ComponentIngredient(List.of(children), predicate, expectedTypes)).toVanilla();
        }
    }

    @Override
    public boolean test(ItemStack stack) {
        boolean typeCheck = true;
        for (var type : this.expectedTypes) {
            if (!stack.has(type.value())) typeCheck = false;
        }
        for(Ingredient child : this.children) {
            if (child.test(stack)) {
                return this.components.test(stack.getComponents()) && typeCheck;
            }
        }
        return false;
    }

    @Override
    public Stream<ItemStack> getItems() {
        List<ItemStack> items = new ArrayList<>();
        for (ItemStack stack : this.children.stream().flatMap((child) -> Arrays.stream(child.getItems())).toList()) {
            stack.applyComponents(components.asPatch());
            items.add(stack);
        }
        return items.stream();
    }

    @Override
    public boolean isSimple() {
        return false;
    }

    @Override
    public IngredientType<?> getType() {
        return AgresticIngredientTypes.COMPONENTS.get();
    }
}
