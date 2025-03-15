package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class TradeableCondition(
    val value: Boolean = true,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val VALUE = "value"
        }

        val CODEC: MapCodec<TradeableCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.BOOL.fieldOf(KEYS.VALUE).orElse(true).forGetter(TradeableCondition::value)
            ).apply(instance, ::TradeableCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return pokemon.tradeable == value
    }

    override fun getType(): LootItemConditionType = LootConditions.TRADEABLE
}