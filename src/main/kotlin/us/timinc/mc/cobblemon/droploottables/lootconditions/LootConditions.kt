package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.loot.condition.LootConditionType
import net.minecraft.loot.context.LootContextParameter
import net.minecraft.registry.Registries
import net.minecraft.registry.Registry
import us.timinc.mc.cobblemon.droploottables.DropLootTables

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
