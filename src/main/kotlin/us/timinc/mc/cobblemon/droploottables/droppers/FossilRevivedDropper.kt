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
            val position = player.position()
            val pokemon = event.pokemon
            val pokemonEntity = pokemon.entity
            val form = pokemon.form

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.RELEVANT_PLAYER to player,
                ),
                mapOf(),
                player.luck
            )
            val context = FormDropContext(form)
            val drops = getDrops(
                lootParams,
                context
            )

            giveDropsToPlayer(drops, player)
        }
    }
}