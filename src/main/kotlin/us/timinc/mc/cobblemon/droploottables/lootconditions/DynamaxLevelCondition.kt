package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.toIntRange

class DynamaxLevelCondition(
    val range: IntRange,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
        }

        val CODEC: MapCodec<DynamaxLevelCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() }
            ).apply(instance) { DynamaxLevelCondition(toIntRange(it)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonLevel = pokemon.dmaxLevel
        return range.contains(pokemonLevel)
    }

    override fun getType(): LootItemConditionType = LootConditions.DYNAMAX_LEVEL
}