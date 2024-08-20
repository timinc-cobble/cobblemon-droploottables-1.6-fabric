package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.server
import net.fabricmc.api.ModInitializer
import net.minecraft.loot.LootDataType
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import us.timinc.mc.cobblemon.droploottables.implementation.cobblemon.dropentries.DynamicItemDropEntry
import us.timinc.mc.cobblemon.droploottables.implementation.minecraft.lootconditions.LootConditions

object DropLootTables : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    const val MOD_ID = "droploottables"

    override fun onInitialize() {
        LootConditions.register()
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.LOWEST) { event ->
            val world = event.player?.world ?: event.entity?.world ?: server()?.overworld ?: return@subscribe
            if (world.server == null) return@subscribe
            val dropperEntity = event.entity ?: return@subscribe
            if (dropperEntity !is PokemonEntity) return@subscribe

            val lootManager = world.server!!.lootManager

            val position = dropperEntity.blockPos
            val vecPos = Vec3d(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())

            val pokemon = dropperEntity.pokemon

            val lootContextParameterSet = LootContextParameterSet(
                world as ServerWorld, mapOf(
                    LootContextParameters.ORIGIN to vecPos,
                    LootContextParameters.KILLER_ENTITY to event.player,
                    LootConditions.PARAMS.SLAIN_POKEMON to pokemon
                ), mapOf(), 0F
            )

            val speciesDropId =
                modIdentifier("gameplay/pokedrops/species/${pokemon.species.resourceIdentifier.path}")
            if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(speciesDropId)) {
                println("Switching to loot table drops for species ${pokemon.species.resourceIdentifier.path}...")
                val lootTable = lootManager.getLootTable(speciesDropId)
                val list = lootTable.generateLoot(
                    lootContextParameterSet
                )

                event.drops.clear()
                event.drops.addAll(list.map(::DynamicItemDropEntry))
            }

            val globalDropId = modIdentifier("gameplay/pokedrops/all")
            if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(globalDropId)) {
                val lootTable = lootManager.getLootTable((globalDropId))
                val list = lootTable.generateLoot(
                    lootContextParameterSet
                )

                event.drops.addAll(list.map(::DynamicItemDropEntry))
            }
        }
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val world = event.player.world
            if (world.server == null) return@subscribe
            val pokemon = event.pokemon

            val lootManager = world.server!!.lootManager

            val position = event.player.blockPos
            val vecPos = Vec3d(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())

            val lootContextParameterSet = LootContextParameterSet(
                world as ServerWorld, mapOf(
                    LootContextParameters.ORIGIN to vecPos,
                    LootContextParameters.KILLER_ENTITY to event.player,
                    LootConditions.PARAMS.SLAIN_POKEMON to pokemon
                ), mapOf(), 0F
            )

            val speciesDropId =
                modIdentifier("gameplay/releasedrops/species/${pokemon.species.resourceIdentifier.path}")
            if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(speciesDropId)) {
                println("Switching to loot table drops for species ${pokemon.species.resourceIdentifier.path}...")
                val lootTable = lootManager.getLootTable(speciesDropId)
                val list = lootTable.generateLoot(
                    lootContextParameterSet
                )
                list.forEach { event.player.giveItemStack(it) }
            }

            val globalDropId = modIdentifier("gameplay/releasedrops/all")
            if (lootManager.getIds(LootDataType.LOOT_TABLES).contains(globalDropId)) {
                val lootTable = lootManager.getLootTable((globalDropId))
                val list = lootTable.generateLoot(
                    lootContextParameterSet
                )
                list.forEach { event.player.giveItemStack(it) }
            }
        }
    }

    fun modIdentifier(name: String): Identifier {
        return Identifier(MOD_ID, name)
    }
}