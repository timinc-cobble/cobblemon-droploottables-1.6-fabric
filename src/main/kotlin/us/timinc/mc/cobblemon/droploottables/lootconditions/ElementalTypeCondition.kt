package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class ElementalTypeCondition(
    val elements: List<ElementalType>,
    val all: Boolean = false
) : LootItemCondition {
    companion object {
        object KEYS {
            const val ELEMENTAL_TYPE = "elements"
            const val ALL = "all"
        }

        val CODEC: MapCodec<ElementalTypeCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ElementalType.BY_STRING_CODEC.listOf().fieldOf(KEYS.ELEMENTAL_TYPE)
                    .forGetter(ElementalTypeCondition::elements),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(ElementalTypeCondition::all)
            ).apply(instance, ::ElementalTypeCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonTypes = pokemon.types
        return if (all) elements.all(pokemonTypes::contains) else elements.any(pokemonTypes::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.TYPE
}