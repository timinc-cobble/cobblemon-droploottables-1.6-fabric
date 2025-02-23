package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.giveOrDropItemStack
import com.cobblemon.mod.common.util.server
import net.fabricmc.api.ModInitializer
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import net.minecraft.world.phys.Vec3
import us.timinc.mc.cobblemon.droploottables.config.ConfigBuilder
import us.timinc.mc.cobblemon.droploottables.config.MainConfig
import us.timinc.mc.cobblemon.droploottables.dropentries.DynamicItemDropEntry
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

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
            val world = event.player?.level() ?: event.entity?.level() ?: server()?.overworld() ?: return@subscribe
            if (world !is ServerLevel) return@subscribe
            val dropperEntity = event.entity ?: return@subscribe
            if (dropperEntity !is PokemonEntity) return@subscribe

            val (results, resultType) = getDrops(
                world,
                dropperEntity.position(),
                dropperEntity.pokemon,
                "pokedrops",
                event.player,
            )
            if (resultType === Result.REPLACE) event.drops.clear()
            event.drops.addAll(results.map(::DynamicItemDropEntry))
        }
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val world = event.player.level()
            if (world !is ServerLevel) return@subscribe
            val (results) = getDrops(
                world,
                event.player.position(),
                event.pokemon,
                "releasedrops",
                event.player,
            )
            results.forEach(event.player::giveOrDropItemStack)
        }
    }

    fun getDrops(
        world: ServerLevel,
        position: Vec3,
        pokemon: Pokemon,
        dropType: String,
        player: ServerPlayer?,
    ): Pair<List<ItemStack>, Result> {
        val lootManager = world.server.reloadableRegistries().getKeys(Registries.LOOT_TABLE)
        var result = Result.NOTHING

        val lootContextParameterSet = LootParams(
            world, mapOf(
                LootContextParams.ORIGIN to position,
                LootContextParams.ATTACKING_ENTITY to player,
                LootConditions.PARAMS.SLAIN_POKEMON to pokemon,
                LootContextParams.THIS_ENTITY to pokemon.entity,
                LootContextParams.DIRECT_ATTACKING_ENTITY to player,
                LootContextParams.LAST_DAMAGE_PLAYER to pokemon.entity?.lastAttacker
            ), mapOf(), 0F
        )

        val results: MutableList<ItemStack> = mutableListOf()
        val speciesDropId =
            modIdentifier("gameplay/$dropType/species/${pokemon.species.resourceIdentifier.path}")
        if (lootManager.contains(speciesDropId)) {
            result = Result.REPLACE
            debug("Switching to loot table drops $dropType for species ${pokemon.species.resourceIdentifier.path}...")
            val lootTable = world.server.reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, speciesDropId)
            )
            val list = lootTable.getRandomItems(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        val globalDropId = modIdentifier("gameplay/$dropType/all")
        if (lootManager.contains(globalDropId)) {
            if (result === Result.NOTHING) result = Result.ADD
            val lootTable = world.server.reloadableRegistries().getLootTable(
                ResourceKey.create(Registries.LOOT_TABLE, globalDropId)
            )
            val list = lootTable.getRandomItems(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        debug("Drop results: ($result) ${results.map { it.displayName.string }}")
        return Pair(results, result)
    }

    fun modIdentifier(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }

    fun debug(msg: String) {
        if (!config.debug) {
            return
        }
        println(msg)
    }
}