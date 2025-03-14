package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object CaptureDropper : AbstractFormDropper("capture") {
    override fun load() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            val pokemonEntity = event.pokemon.entity
            val player = event.player
            val positionalEntity = pokemonEntity ?: player
            val level = positionalEntity.level()
            if (level !is ServerLevel) return@subscribe
            val position = positionalEntity.position()
            val pokemon = event.pokemon
            val pokeBall = event.pokeBallEntity.pokeBall

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.RELEVANT_PLAYER to player,
                    LootConditions.PARAMS.POKE_BALL to pokeBall
                ),
                mapOf(),
                player.luck
            )
            val context = FormDropContext(event.pokemon.form)
            val drops = getDrops(
                lootParams,
                context
            )

            giveDropsToPlayer(drops, event.player)
        }
    }
}