package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class PrimaryElementalTypeCondition(
    val elements: List<ElementalType>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val ELEMENTAL_TYPE = "elements"
        }

        val CODEC: MapCodec<ElementalTypeCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ElementalType.BY_STRING_CODEC.listOf().fieldOf(KEYS.ELEMENTAL_TYPE)
                    .forGetter(ElementalTypeCondition::elements)
            ).apply(instance, ::ElementalTypeCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonPrimaryType = pokemon.types.iterator().next()
        return elements.any { it == pokemonPrimaryType }
    }

    override fun getType(): LootItemConditionType = LootConditions.PRIMARY_TYPE
}