package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.cobblemon.mod.common.util.server
import net.fabricmc.api.ModInitializer
import net.minecraft.item.ItemStack
import net.minecraft.loot.LootDataType
import net.minecraft.loot.LootTable
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.registry.BuiltinRegistries
import net.minecraft.registry.Registries
import net.minecraft.registry.RegistryKey
import net.minecraft.registry.RegistryKeys
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import us.timinc.mc.cobblemon.droploottables.config.ConfigBuilder
import us.timinc.mc.cobblemon.droploottables.config.MainConfig
import us.timinc.mc.cobblemon.droploottables.implementation.cobblemon.dropentries.DynamicItemDropEntry
import us.timinc.mc.cobblemon.droploottables.implementation.minecraft.lootconditions.LootConditions

enum class Result {
    REPLACE,
    ADD,
    NOTHING
}

object DropLootTables : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    const val MOD_ID = "droploottables"
    lateinit var config: MainConfig

    override fun onInitialize() {
        config = ConfigBuilder.load(MainConfig::class.java, MOD_ID)
        LootConditions.register()
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.LOWEST) { event ->
            val world = event.player?.world ?: event.entity?.world ?: server()?.overworld ?: return@subscribe
            if (world !is ServerWorld) return@subscribe
            val dropperEntity = event.entity ?: return@subscribe
            if (dropperEntity !is PokemonEntity) return@subscribe

            val (results, resultType) = getDrops(
                world,
                dropperEntity.pos,
                dropperEntity.pokemon,
                "pokedrops",
                event.player,
            )
            if (resultType === Result.REPLACE) event.drops.clear()
            event.drops.addAll(results.map(::DynamicItemDropEntry))
        }
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val world = event.player.world
            if (world !is ServerWorld) return@subscribe
            val (results) = getDrops(
                world,
                event.player.pos,
                event.pokemon,
                "releasedrops",
                event.player,
            )
            results.forEach(event.player::giveOrDropItemStack)
        }
    }

    fun getDrops(
        world: ServerWorld,
        position: Vec3d,
        pokemon: Pokemon,
        dropType: String,
        player: ServerPlayerEntity?,
    ): Pair<List<ItemStack>, Result> {
        val lootManager = world.server.reloadableRegistries.getIds(RegistryKeys.LOOT_TABLE)
        var result = Result.NOTHING

        val lootContextParameterSet = LootContextParameterSet(
            world, mapOf(
                LootContextParameters.ORIGIN to position,
                LootContextParameters.ATTACKING_ENTITY to player,
                LootConditions.PARAMS.SLAIN_POKEMON to pokemon,
                LootContextParameters.THIS_ENTITY to pokemon.entity,
                LootContextParameters.DIRECT_ATTACKING_ENTITY to player,
                LootContextParameters.LAST_DAMAGE_PLAYER to pokemon.entity?.lastAttacker
            ), mapOf(), 0F
        )

        val results: MutableList<ItemStack> = mutableListOf()
        val speciesDropId =
            modIdentifier("gameplay/$dropType/species/${pokemon.species.resourceIdentifier.path}")
        if (lootManager.contains(speciesDropId)) {
            result = Result.REPLACE
            debug("Switching to loot table drops $dropType for species ${pokemon.species.resourceIdentifier.path}...")
            val lootTable = world.server.reloadableRegistries.getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, speciesDropId))
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        val globalDropId = modIdentifier("gameplay/$dropType/all")
        if (lootManager.contains(globalDropId)) {
            if (result === Result.NOTHING) result = Result.ADD
            val lootTable = world.server.reloadableRegistries.getLootTable(RegistryKey.of(RegistryKeys.LOOT_TABLE, globalDropId))
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        debug("Drop results: ($result) ${results.map { it.name.string }}")
        return Pair(results, result)
    }

    fun modIdentifier(name: String): Identifier {
        return Identifier.of(MOD_ID, name)
    }

    fun debug(msg: String) {
        if (!config.debug) {
            return
        }
        println(msg)
    }
}