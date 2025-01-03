package us.timinc.mc.cobblemon.droploottables.implementation.minecraft.lootconditions

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.api.types.ElementalTypes
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.toProperties
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonObject
import com.google.gson.JsonSerializationContext
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.ListCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.loot.condition.LootCondition
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContext
import net.minecraft.loot.context.LootContextParameter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import us.timinc.mc.cobblemon.droploottables.DropLootTables
import us.timinc.mc.cobblemon.droploottables.toIntRange

object LootConditions {
    val POKEMON_PROPERTIES: LootConditionType = LootConditionType(PokemonPropertiesLootCondition.CODEC)
    val POKEMON_LEVEL: LootConditionType = LootConditionType(PokemonLevelLootCondition.CODEC)
    val POKEMON_TYPE: LootConditionType = LootConditionType(PokemonElementalTypeLootCondition.CODEC)

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
        Registry.register(
            Registries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_type"), POKEMON_TYPE
        )
    }
}

class PokemonPropertiesLootCondition(
    val properties: PokemonProperties,
) : LootCondition {
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
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        return properties.matches(pokemon)
    }

    override fun getType(): LootConditionType {
        return LootConditions.POKEMON_PROPERTIES
    }
}

class PokemonLevelLootCondition(
    val range: IntRange,
) : LootCondition {
    companion object {
        object KEYS {
            const val RANGE = "range"
        }

        val CODEC: MapCodec<PokemonLevelLootCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.RANGE).forGetter { it.range.toString() }
            ).apply(instance) { PokemonLevelLootCondition(toIntRange(it)) }
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

class PokemonElementalTypeLootCondition(
    val types: List<ElementalType>,
) : LootCondition {
    companion object {
        object KEYS {
            const val ELEMENTAL_TYPE = "element"
        }

        val CODEC: MapCodec<PokemonElementalTypeLootCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ElementalType.BY_STRING_CODEC.listOf().fieldOf(KEYS.ELEMENTAL_TYPE).forGetter(PokemonElementalTypeLootCondition::types)
            ).apply(instance, ::PokemonElementalTypeLootCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.get(LootConditions.PARAMS.SLAIN_POKEMON)!!
        val pokemonTypes = pokemon.types
        return types.any(pokemonTypes::contains)
    }

    override fun getType(): LootConditionType = LootConditions.POKEMON_TYPE
}