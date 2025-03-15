package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class AbilityCondition(
    val abilities: List<String>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val ABILITIES = "abilities"
        }

        val CODEC: MapCodec<AbilityCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.ABILITIES).forGetter(AbilityCondition::abilities)
            ).apply(instance, ::AbilityCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return abilities.contains(pokemon.ability.name)
    }

    override fun getType(): LootItemConditionType = LootConditions.ABILITY
}