package us.timinc.mc.cobblemon.droploottables.lootconditions.counter

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.counter.CounterMod
import us.timinc.mc.cobblemon.counter.api.CounterType
import us.timinc.mc.cobblemon.counter.extensions.getCounterManager
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

class CounterCondition(
    val min: Int,
    val counterType: CounterType,
    val streak: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val MIN = "min"
            const val COUNTER_TYPE = "counter_type"
            const val STREAK = "streak"
        }

        val CODEC: MapCodec<CounterCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.INT.fieldOf(KEYS.MIN).forGetter(CounterCondition::min),
                Codec.STRING.fieldOf(KEYS.COUNTER_TYPE).forGetter { it.counterType.type },
                Codec.BOOL.fieldOf(KEYS.STREAK).orElse(false).forGetter(CounterCondition::streak)
            ).apply(instance) { min, counterType, streak ->
                CounterCondition(
                    min,
                    CounterType.entries.find { it.type == counterType }
                        ?: throw Error("Counter type $counterType not found"),
                    streak
                )
            }
        }
    }

    override fun test(context: LootContext): Boolean {
        val player = context.getParamOrNull(LootContextParams.DIRECT_ATTACKING_ENTITY) ?: return false
        if (player !is ServerPlayer) return false
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

        return value >= min
    }

    override fun getType(): LootItemConditionType = CounterLootConditions.COUNT_CONDITION
}