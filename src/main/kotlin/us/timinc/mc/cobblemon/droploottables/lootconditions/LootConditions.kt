package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.storage.loot.parameters.LootContextParam
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.DropLootTables

object LootConditions {
    val POKEMON_PROPERTIES: LootItemConditionType = LootItemConditionType(PokemonPropertiesLootCondition.CODEC)
    val POKEMON_LEVEL: LootItemConditionType = LootItemConditionType(PokemonLevelLootCondition.CODEC)
    val POKEMON_TYPE: LootItemConditionType = LootItemConditionType(PokemonElementalTypeLootCondition.CODEC)
    val WAS_IN_BATTLE: LootItemConditionType = LootItemConditionType(WasInBattleCondition.CODEC)
    val POKEMON_LABEL: LootItemConditionType = LootItemConditionType(PokemonLabelCondition.CODEC)
    val EGG_GROUP: LootItemConditionType = LootItemConditionType(EggGroupCondition.CODEC)

    object PARAMS {
        val POKEMON_DETAILS: LootContextParam<Pokemon> =
            LootContextParam(DropLootTables.modIdentifier("pokemon"))
        val WAS_IN_BATTLE: LootContextParam<Boolean> =
            LootContextParam(DropLootTables.modIdentifier("was_in_battle"))
    }

    fun register() {
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("pokemon_properties"),
            POKEMON_PROPERTIES
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_level"), POKEMON_LEVEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_type"), POKEMON_TYPE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("was_in_battle"), WAS_IN_BATTLE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("pokemon_label"), POKEMON_LABEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("egg_group"), EGG_GROUP
        )
    }
}
