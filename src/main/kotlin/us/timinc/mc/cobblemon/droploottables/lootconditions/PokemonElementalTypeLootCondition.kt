package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class PokemonElementalTypeLootCondition(
    val types: List<ElementalType>,
) : LootItemCondition {
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
        val pokemon: Pokemon = context.getParam(LootConditions.PARAMS.POKEMON_DETAILS)!!
        val pokemonTypes = pokemon.types
        return types.any(pokemonTypes::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.POKEMON_TYPE
}