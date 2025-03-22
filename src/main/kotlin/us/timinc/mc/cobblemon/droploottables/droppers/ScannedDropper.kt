package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object ScannedDropper : AbstractFormDropper("scan") {
    override fun load() {
        CobblemonEvents.POKEMON_SCANNED.subscribe { event ->
            val pokemonEntity = event.scannedEntity as? PokemonEntity ?: return@subscribe
            val level = pokemonEntity.level() as? ServerLevel ?: return@subscribe
            val player = event.player
            val position = pokemonEntity.position()
            val pokemon = pokemonEntity.pokemon

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
            val context = FormDropContext(pokemon.form)
            val drops = getDrops(
                lootParams,
                context
            )

            giveDropsToPlayer(drops, player)
        }
    }
}