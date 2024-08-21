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
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import us.timinc.mc.cobblemon.droploottables.config.ConfigBuilder
import us.timinc.mc.cobblemon.droploottables.config.MainConfig
import us.timinc.mc.cobblemon.droploottables.implementation.cobblemon.dropentries.DynamicItemDropEntry
import us.timinc.mc.cobblemon.droploottables.implementation.minecraft.lootconditions.LootConditions

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

            val results = getDrops(
                world,
                dropperEntity.pos,
                dropperEntity.pokemon,
                "pokedrops",
                event.player,
            )
            event.drops.clear()
            event.drops.addAll(results.map(::DynamicItemDropEntry))
        }
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val world = event.player.world
            if (world !is ServerWorld) return@subscribe
            val results = getDrops(
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
    ): List<ItemStack> {
        val lootManager = world.server.lootManager

        val lootContextParameterSet = LootContextParameterSet(
            world, mapOf(
                LootContextParameters.ORIGIN to position,
                LootContextParameters.KILLER_ENTITY to player,
                LootConditions.PARAMS.SLAIN_POKEMON to pokemon
            ), mapOf(), 0F
        )

        val results: MutableList<ItemStack> = mutableListOf()
        val speciesDropId =
            modIdentifier("gameplay/$dropType/species/${pokemon.species.resourceIdentifier.path}")
        if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(speciesDropId)) {
            debug("Switching to loot table drops $dropType for species ${pokemon.species.resourceIdentifier.path}...")
            val lootTable = lootManager.getLootTable(speciesDropId)
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        val globalDropId = modIdentifier("gameplay/$dropType/all")
        if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(globalDropId)) {
            val lootTable = lootManager.getLootTable((globalDropId))
            val list = lootTable.generateLoot(
                lootContextParameterSet
            )
            results.addAll(list)
        }

        debug("Drop results: ${results.map { "${it.name.string}${if (it.nbt != null) "{${it.nbt.toString()}}" else ""} x ${it.count}" }}")
        return results
    }

    fun modIdentifier(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }

    fun debug(msg: String) {
        if (!config.debug) {
            return
        }
        println(msg)
    }
}