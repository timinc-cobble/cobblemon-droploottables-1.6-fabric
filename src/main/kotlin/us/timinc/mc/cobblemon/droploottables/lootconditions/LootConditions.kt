package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.evolution.Evolution
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.parameters.LootContextParam
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.DropLootTables

object LootConditions {
    val PERSISTENT_DATA_RANGE: LootItemConditionType = LootItemConditionType(PersistentDataRangeCondition.CODEC)
    val PERSISTENT_DATA: LootItemConditionType = LootItemConditionType(PersistentDataCondition.CODEC)
    val POKEMON_FRIENDSHIP: LootItemConditionType = LootItemConditionType(FriendshipLevelCondition.CODEC)
    val POKEMON_PROPERTIES: LootItemConditionType = LootItemConditionType(PropertiesCondition.CODEC)
    val POKEMON_LEVEL: LootItemConditionType = LootItemConditionType(LevelCondition.CODEC)
    val POKEMON_TYPE: LootItemConditionType = LootItemConditionType(ElementalTypeCondition.CODEC)
    val WAS_IN_BATTLE: LootItemConditionType = LootItemConditionType(WasInBattleCondition.CODEC)
    val POKEMON_LABEL: LootItemConditionType = LootItemConditionType(LabelCondition.CODEC)
    val EGG_GROUP: LootItemConditionType = LootItemConditionType(EggGroupCondition.CODEC)

    object PARAMS {
        val EVOLUTION: LootContextParam<Evolution> = LootContextParam(DropLootTables.modIdentifier("evolution"))
        val POKEMON_DETAILS: LootContextParam<Pokemon> = LootContextParam(DropLootTables.modIdentifier("pokemon"))
        val WAS_IN_BATTLE: LootContextParam<Boolean> = LootContextParam(DropLootTables.modIdentifier("was_in_battle"))
        val RELEVANT_PLAYER: LootContextParam<ServerPlayer> =
            LootContextParam(DropLootTables.modIdentifier("relevant_player"))
    }

    fun register() {
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("properties"),
            POKEMON_PROPERTIES
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("level"), POKEMON_LEVEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("type"), POKEMON_TYPE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("was_in_battle"), WAS_IN_BATTLE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("label"), POKEMON_LABEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("egg_group"), EGG_GROUP
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("friendship"), POKEMON_FRIENDSHIP
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("persistent_data"), PERSISTENT_DATA
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("persistent_data_range"),
            PERSISTENT_DATA_RANGE
        )
    }
}
