package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.extensions.hasHiddenAbility

class HiddenAbilityCondition(
    val value: Boolean = true
): LootItemCondition {
    companion object {
        object KEYS {
            const val VALUE = "value"
        }

        val CODEC: MapCodec<HiddenAbilityCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.BOOL.fieldOf(KEYS.VALUE).orElse(true).forGetter(HiddenAbilityCondition::value)
            ).apply(instance, ::HiddenAbilityCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return pokemon.hasHiddenAbility() == value
    }

    override fun getType(): LootItemConditionType = LootConditions.HIDDEN_ABILITY
}