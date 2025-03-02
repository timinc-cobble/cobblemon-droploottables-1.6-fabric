package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.toIntRange

class PokemonLevelLootCondition(
    val range: IntRange,
) : LootItemCondition {
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
        val pokemon: Pokemon = context.getParam(LootConditions.PARAMS.POKEMON_DETAILS)!!
        val pokemonLevel = pokemon.level
        return range.contains(pokemonLevel)
    }

    override fun getType(): LootItemConditionType {
        return LootConditions.POKEMON_LEVEL
    }
}