package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class AbilityCondition(
    val name: List<String>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val NAME = "name"
        }

        val CODEC: MapCodec<AbilityCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.NAME).forGetter(AbilityCondition::name)
            ).apply(instance, ::AbilityCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return name.contains(pokemon.ability.name)
    }

    override fun getType(): LootItemConditionType = LootConditions.ABILITY
}