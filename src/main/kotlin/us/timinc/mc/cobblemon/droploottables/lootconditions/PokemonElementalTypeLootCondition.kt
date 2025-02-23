package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext

class PokemonElementalTypeLootCondition(
    val types: List<ElementalType>,
) : LootCondition {
    companion object {
        object KEYS {
            const val ELEMENTAL_TYPE = "element"
        }

        val CODEC: MapCodec<PokemonElementalTypeLootCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ElementalType.BY_STRING_CODEC.listOf().fieldOf(KEYS.ELEMENTAL_TYPE)
                    .forGetter(PokemonElementalTypeLootCondition::types)
            ).apply(instance, ::PokemonElementalTypeLootCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        val pokemonTypes = pokemon.types
        return types.any(pokemonTypes::contains)
    }

    override fun getType(): LootConditionType = LootConditions.POKEMON_TYPE
}