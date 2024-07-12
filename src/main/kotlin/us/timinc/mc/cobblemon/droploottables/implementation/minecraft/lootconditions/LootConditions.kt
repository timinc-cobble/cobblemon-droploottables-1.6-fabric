package us.timinc.mc.cobblemon.droploottables.implementation.minecraft.lootconditions

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.pokemon.Pokemon
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import net.minecraft.util.JsonHelper
import net.minecraft.util.JsonSerializer
import us.timinc.mc.cobblemon.droploottables.DropLootTables
import us.timinc.mc.cobblemon.droploottables.toIntRange

object LootConditions {
    val POKEMON_PROPERTIES: LootConditionType = LootConditionType(PokemonPropertiesLootCondition.Companion.Serializer())
    val POKEMON_LEVEL: LootConditionType = LootConditionType(PokemonLevelLootCondition.Companion.Serializer())

    object PARAMS {
        val SLAIN_POKEMON: LootContextParameter<Pokemon> =
            LootContextParameter(DropLootTables.modIdentifier("slain_pokemon"))
    }

    fun register() {
        Registry.register(
            Registries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_properties"), POKEMON_PROPERTIES
        )
        Registry.register(
            Registries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_level"), POKEMON_LEVEL
        )
    }
}

class PokemonPropertiesLootCondition(
    val properties: PokemonProperties
) : LootCondition {
    companion object {
        object KEYS {
            const val PROPERTIES = "properties"
        }

        class Serializer : JsonSerializer<PokemonPropertiesLootCondition> {
            override fun toJson(
                jsonObject: JsonObject,
                lootCondition: PokemonPropertiesLootCondition,
                jsonSerializationContext: JsonSerializationContext
            ) {
                jsonObject.addProperty(KEYS.PROPERTIES, lootCondition.properties.originalString)
            }

            override fun fromJson(
                jsonObject: JsonObject, jsonDeserializationContext: JsonDeserializationContext
            ): PokemonPropertiesLootCondition {
                val pokemonProperties: String = if (jsonObject.has(KEYS.PROPERTIES)) JsonHelper.getString(
                    jsonObject, KEYS.PROPERTIES
                ) else ""
                return PokemonPropertiesLootCondition(PokemonProperties.parse(pokemonProperties))
            }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        return properties.matches(pokemon)
    }

    override fun getType(): LootConditionType {
        return LootConditions.POKEMON_PROPERTIES
    }
}

class PokemonLevelLootCondition(
    val range: IntRange
) : LootCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
        }

        class Serializer : JsonSerializer<PokemonLevelLootCondition> {
            override fun toJson(
                jsonObject: JsonObject,
                lootCondition: PokemonLevelLootCondition,
                jsonSerializationContext: JsonSerializationContext
            ) {
                jsonObject.addProperty(KEYS.RANGE, lootCondition.range.toString())
            }

            override fun fromJson(
                jsonObject: JsonObject, jsonDeserializationContext: JsonDeserializationContext
            ): PokemonLevelLootCondition {
                val range: String = if (jsonObject.has(KEYS.RANGE)) JsonHelper.getString(
                    jsonObject, KEYS.RANGE
                ) else "0..0"
                return PokemonLevelLootCondition(toIntRange(range))
            }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        val pokemonLevel = pokemon.level
        return range.contains(pokemonLevel)
    }

    override fun getType(): LootConditionType {
        return LootConditions.POKEMON_LEVEL
    }
}