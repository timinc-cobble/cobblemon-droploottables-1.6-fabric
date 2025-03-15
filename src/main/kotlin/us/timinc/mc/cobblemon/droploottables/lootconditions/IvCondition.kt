package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.stats.Stats
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.toIntRange

class IvCondition(
    val range: IntRange,
    val stat: String,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
            const val STAT = "stat"
        }

        val CODEC: MapCodec<IvCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() },
                Codec.STRING.fieldOf(KEYS.STAT).forGetter(IvCondition::stat)
            ).apply(instance) { range, stat -> IvCondition(toIntRange(range), stat) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonEv = pokemon.ivs[Stats.getStat(stat)]
        return range.contains(pokemonEv)
    }

    override fun getType(): LootItemConditionType = LootConditions.IV
}