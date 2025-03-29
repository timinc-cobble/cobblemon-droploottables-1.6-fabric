package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object StarterChosenDropper : AbstractFormDropper("starter") {
    override fun load() {
        CobblemonEvents.STARTER_CHOSEN.subscribe { event ->
            val player = event.player
            val level = player.level()
            if (level !is ServerLevel) return@subscribe
            val position = player.position()
            val pokemon = event.pokemon

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon
                ),
                mapOf(),
                player.luck
            )
            val context = FormDropContext(pokemon.form)
            val drops = getDrops(
                lootParams,
                context
            )

            giveDropsToPlayer(drops, player)
        }
    }
}