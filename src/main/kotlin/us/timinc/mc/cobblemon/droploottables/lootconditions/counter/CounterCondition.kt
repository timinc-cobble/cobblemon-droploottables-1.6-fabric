package us.timinc.mc.cobblemon.droploottables.lootconditions.counter

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.counter.CounterMod
import us.timinc.mc.cobblemon.counter.api.CounterType
import us.timinc.mc.cobblemon.counter.extensions.getCounterManager
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions
import us.timinc.mc.cobblemon.droploottables.toIntRange

class CounterCondition(
    val range: IntRange,
    val counterType: CounterType,
    val streak: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
            const val COUNTER_TYPE = "counter_type"
            const val STREAK = "streak"
        }

        val CODEC: MapCodec<CounterCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() },
                Codec.STRING.fieldOf(KEYS.COUNTER_TYPE).forGetter { it.counterType.type },
                Codec.BOOL.fieldOf(KEYS.STREAK).orElse(false).forGetter(CounterCondition::streak)
            ).apply(instance) { range, counterType, streak ->
                CounterCondition(
                    toIntRange(range),
                    CounterType.entries.find { it.type == counterType }
                        ?: throw Error("Counter type $counterType not found"),
                    streak
                )
            }
        }
    }

    override fun test(context: LootContext): Boolean {
        val player = context.getParamOrNull(LootConditions.PARAMS.RELEVANT_PLAYER) ?: return false
        val pokemonData = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false

        val speciesId = pokemonData.species.resourceIdentifier
        val formName = pokemonData.form.name

        val manager = player.getCounterManager()
        val value =
            if (streak) {
                val streakData = manager.getStreak(counterType)
                // TODO: This is horrible. Make a simple manager.getStreak(counterType, species, formName) in Counter.
                if (!streakData.wouldBreak(speciesId, formName, CounterMod.config.breakStreakOnForm(counterType))) {
                    streakData.count
                } else {
                    0
                }
            } else {
                manager.getCount(counterType, speciesId, formName)
            }

        return range.contains(value)
    }

    override fun getType(): LootItemConditionType = CounterLootConditions.COUNT_CONDITION
}