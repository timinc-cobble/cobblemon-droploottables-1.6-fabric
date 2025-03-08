package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.toProperties
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class PokemonPropertiesLootCondition(
    val properties: PokemonProperties,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val PROPERTIES = "properties"
        }

        val CODEC: MapCodec<PokemonPropertiesLootCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.PROPERTIES).forGetter { it.properties.originalString }
            ).apply(instance) { PokemonPropertiesLootCondition(it.toProperties()) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParam(LootConditions.PARAMS.POKEMON_DETAILS)!!
        return properties.matches(pokemon)
    }

    override fun getType(): LootItemConditionType {
        return LootConditions.POKEMON_PROPERTIES
    }
}