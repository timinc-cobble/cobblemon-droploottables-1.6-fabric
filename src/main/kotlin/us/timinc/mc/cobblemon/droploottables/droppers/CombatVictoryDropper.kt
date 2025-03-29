package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object CombatVictoryDropper : AbstractFormDropper("victory") {
    override fun load() {
        CobblemonEvents.BATTLE_VICTORY.subscribe { event ->
            val level =
                event.winners.flatMap { it.pokemonList }.firstNotNullOfOrNull { it.entity?.level() } ?: return@subscribe
            if (level !is ServerLevel) return@subscribe
            for (winner in event.winners) {
                for (battlePokemon in winner.pokemonList) {
                    val pokemon = battlePokemon.effectedPokemon
                    val player = pokemon.getOwnerPlayer() ?: continue
                    val wasInBattle = battlePokemon.facedOpponents.isNotEmpty()
                    processDrop(level, player, pokemon, wasInBattle)
                }
            }
        }
    }

    private fun processDrop(level: ServerLevel, player: ServerPlayer, pokemon: Pokemon, wasInBattle: Boolean) {
        if (!pokemon.heldItem().isEmpty) return

        val lootParams = LootParams(
            level,
            mapOf(
                LootContextParams.ORIGIN to player.position(),
                LootContextParams.THIS_ENTITY to player,
                LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                LootConditions.PARAMS.WAS_IN_BATTLE to wasInBattle
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