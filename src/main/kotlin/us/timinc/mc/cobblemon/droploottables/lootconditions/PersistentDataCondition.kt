package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class PersistentDataCondition(
    val key: String,
    val value: String,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val KEY = "key"
            const val VALUE = "value"
        }

        val CODEC: MapCodec<PersistentDataCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.KEY).forGetter(PersistentDataCondition::key),
                Codec.STRING.fieldOf(KEYS.VALUE).forGetter(PersistentDataCondition::value)
            ).apply(instance, ::PersistentDataCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val tagValue = pokemon.persistentData.get(key)?.asString ?: return false
        return tagValue == value
    }

    override fun getType(): LootItemConditionType = LootConditions.PERSISTENT_DATA
}