package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object FossilRevivedDropper : AbstractFormDropper("resurrect") {
    override fun load() {
        CobblemonEvents.FOSSIL_REVIVED.subscribe { event ->
            val level = event.player?.level() ?: return@subscribe
            if (level !is ServerLevel) return@subscribe
            val player = event.player ?: return@subscribe

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to player.position(),
                    LootContextParams.THIS_ENTITY to player,
                    LootConditions.PARAMS.POKEMON_DETAILS to event.pokemon
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