package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class WasInBattleCondition(
    val value: Boolean = true,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val VALUE = "value"
        }

        val CODEC: MapCodec<WasInBattleCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.BOOL.fieldOf(KEYS.VALUE).orElse(true).forGetter(WasInBattleCondition::value)
            ).apply(instance, ::WasInBattleCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        return context.getParamOrNull(LootConditions.PARAMS.WAS_IN_BATTLE) == value
    }

    override fun getType(): LootItemConditionType = LootConditions.WAS_IN_BATTLE
}