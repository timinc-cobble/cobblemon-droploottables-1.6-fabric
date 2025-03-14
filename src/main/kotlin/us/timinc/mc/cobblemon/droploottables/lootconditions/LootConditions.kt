package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.evolution.Evolution
import com.cobblemon.mod.common.pokeball.PokeBall
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
    val FRIENDSHIP: LootItemConditionType = LootItemConditionType(FriendshipLevelCondition.CODEC)
    val PROPERTIES: LootItemConditionType = LootItemConditionType(PropertiesCondition.CODEC)
    val LEVEL: LootItemConditionType = LootItemConditionType(LevelCondition.CODEC)
    val TYPE: LootItemConditionType = LootItemConditionType(ElementalTypeCondition.CODEC)
    val WAS_IN_BATTLE: LootItemConditionType = LootItemConditionType(WasInBattleCondition.CODEC)
    val LABEL: LootItemConditionType = LootItemConditionType(LabelCondition.CODEC)
    val EGG_GROUP: LootItemConditionType = LootItemConditionType(EggGroupCondition.CODEC)

    object PARAMS {
        val POKE_BALL: LootContextParam<PokeBall> = LootContextParam(DropLootTables.modIdentifier("poke_ball"))
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
            PROPERTIES
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("level"), LEVEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("type"), TYPE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("was_in_battle"), WAS_IN_BATTLE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("label"), LABEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("egg_group"), EGG_GROUP
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("friendship"), FRIENDSHIP
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
