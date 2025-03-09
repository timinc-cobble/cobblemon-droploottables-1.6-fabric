package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
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
            val drops = getDrops(
                lootParams,
                FormDropContext(event.pokemon.form)
            )

            giveDropsToPlayer(drops, player)
        }
    }
}