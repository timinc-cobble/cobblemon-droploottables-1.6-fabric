package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import us.timinc.mc.cobblemon.droploottables.toIntRange

class PokemonLevelLootCondition(
    val range: IntRange,
) : LootCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
        }

        val CODEC: MapCodec<PokemonLevelLootCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() }
            ).apply(instance) { PokemonLevelLootCondition(toIntRange(it)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        val pokemonLevel = pokemon.level
        return range.contains(pokemonLevel)
    }

    override fun getType(): LootConditionType {
        return LootConditions.POKEMON_LEVEL
    }
}