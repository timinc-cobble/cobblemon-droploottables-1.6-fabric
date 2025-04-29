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
    val KNOWLEDGE_LEVEL: LootItemConditionType = LootItemConditionType(KnowledgeLevelCondition.CODEC)
    val SHINY: LootItemConditionType = LootItemConditionType(ShinyCondition.CODEC)
    val CAUGHT_BALL: LootItemConditionType = LootItemConditionType(CaughtBallCondition.CODEC)
    val NATURE: LootItemConditionType = LootItemConditionType(NatureCondition.CODEC)
    val HIDDEN_ABILITY: LootItemConditionType = LootItemConditionType(HiddenAbilityCondition.CODEC)
    val GENDER: LootItemConditionType = LootItemConditionType(GenderCondition.CODEC)
    val EV: LootItemConditionType = LootItemConditionType(EvCondition.CODEC)
    val IV: LootItemConditionType = LootItemConditionType(IvCondition.CODEC)
    val ABILITY: LootItemConditionType = LootItemConditionType(AbilityCondition.CODEC)
    val STATUS: LootItemConditionType = LootItemConditionType(StatusCondition.CODEC)
    val NICKNAME: LootItemConditionType = LootItemConditionType(NicknameCondition.CODEC)
    val TERA_TYPE: LootItemConditionType = LootItemConditionType(TeraTypeCondition.CODEC)
    val DYNAMAX_LEVEL: LootItemConditionType = LootItemConditionType(DynamaxLevelCondition.CODEC)
    val GMAX: LootItemConditionType = LootItemConditionType(GmaxCondition.CODEC)
    val TRADEABLE: LootItemConditionType = LootItemConditionType(TradeableCondition.CODEC)
    val MOVE_TYPES: LootItemConditionType = LootItemConditionType(MoveTypesCondition.CODEC)
    val MOVES: LootItemConditionType = LootItemConditionType(MovesCondition.CODEC)
    val ORIGINAL_TRAINER: LootItemConditionType = LootItemConditionType(OriginalTrainerCondition.CODEC)
    val HELD_ITEM: LootItemConditionType = LootItemConditionType(HeldItemCondition.CODEC)
    val ASPECTS: LootItemConditionType = LootItemConditionType(AspectsCondition.CODEC)
    val PERSISTENT_DATA_RANGE: LootItemConditionType = LootItemConditionType(PersistentDataRangeCondition.CODEC)
    val PERSISTENT_DATA: LootItemConditionType = LootItemConditionType(PersistentDataCondition.CODEC)
    val FRIENDSHIP: LootItemConditionType = LootItemConditionType(FriendshipLevelCondition.CODEC)
    val PROPERTIES: LootItemConditionType = LootItemConditionType(PropertiesCondition.CODEC)
    val LEVEL: LootItemConditionType = LootItemConditionType(LevelCondition.CODEC)
    val TYPE: LootItemConditionType = LootItemConditionType(ElementalTypeCondition.CODEC)
    val PRIMARY_TYPE: LootItemConditionType = LootItemConditionType(PrimaryElementalTypeCondition.CODEC)
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
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("primary_type"), PRIMARY_TYPE
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
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("aspects"),
            ASPECTS
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("held_item"),
            HELD_ITEM
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("original_trainer"),
            ORIGINAL_TRAINER
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("moves"),
            MOVES
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("move_types"),
            MOVE_TYPES
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("tradeable"),
            TRADEABLE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("gmax"),
            GMAX
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("dynamax_level"),
            DYNAMAX_LEVEL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("tera_type"),
            TERA_TYPE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("nickname"),
            NICKNAME
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("status"),
            STATUS
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("ability"),
            ABILITY
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("ev"),
            EV
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("iv"),
            IV
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("gender"),
            GENDER
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("hidden_ability"),
            HIDDEN_ABILITY
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("nature"),
            NATURE
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("caught_ball"),
            CAUGHT_BALL
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("shiny"),
            SHINY
        )
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE,
            DropLootTables.modIdentifier("knowledge_level"),
            KNOWLEDGE_LEVEL
        )
    }
}
