package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.drop.ItemDropEntry
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.config
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object EvolutionDropper : AbstractFormDropper("evolve") {
    override fun load() {
        CobblemonEvents.EVOLUTION_COMPLETE.subscribe(Priority.LOWEST) { event ->
            val dropperEntity = event.pokemon.entity
            val player = event.pokemon.getOwnerPlayer() ?: return@subscribe
            val positionalEntity = dropperEntity ?: player
            val level = positionalEntity.level()
            if (level !is ServerLevel) return@subscribe
            val position = positionalEntity.position()
            val form = event.pokemon.form

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to dropperEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to event.pokemon,
                    LootConditions.PARAMS.EVOLUTION to event.evolution
                ),
                mapOf(),
                player.luck
            )
            val finalDrops: MutableList<ItemStack> = mutableListOf()
            val tableDrops = getDrops(
                lootParams,
                FormDropContext(form)
            )
            finalDrops.addAll(tableDrops)

            if (!lootTableExists(level, getFormDropId(form)) && config.useCobblemonDropsIfOverrideNotPresent) {
                val cobbleDrops = event.evolution.drops.getDrops(pokemon = event.pokemon)
                finalDrops.addAll(cobbleDrops.mapNotNull { drop ->
                    if (drop is ItemDropEntry) {
                        val item = level.registryAccess().registryOrThrow(Registries.ITEM).get(drop.item)
                        if (item === null) {
                            debug("Unable to drop item ${drop.item}")
                            return@mapNotNull null
                        }
                        return@mapNotNull ItemStack(item, drop.quantityRange?.random() ?: drop.quantity)
                    }
                    drop.drop(null, level, position, player)
                    return@mapNotNull null
                })
            }

            giveDropsToPlayer(finalDrops, player)
        }
    }
}