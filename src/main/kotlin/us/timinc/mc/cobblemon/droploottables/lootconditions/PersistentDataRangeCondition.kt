package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.math.FloatRange
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.toFloatRange

class PersistentDataRangeCondition(
    val key: String,
    val range: FloatRange,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val KEY = "key"
            const val RANGE = "range"
        }

        val CODEC: MapCodec<PersistentDataRangeCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.KEY).forGetter(PersistentDataRangeCondition::key),
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() }
            ).apply(instance) { key, range -> PersistentDataRangeCondition(key, toFloatRange(range)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val tagStringValue = pokemon.persistentData.get(key)?.asString ?: return false
        val tagValue = tagStringValue.toFloatOrNull() ?: return false
        return range.contains(tagValue)
    }

    override fun getType(): LootItemConditionType = LootConditions.PERSISTENT_DATA_RANGE
}