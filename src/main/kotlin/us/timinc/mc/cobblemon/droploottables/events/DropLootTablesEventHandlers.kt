package us.timinc.mc.cobblemon.droploottables.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress
import net.minecraft.server.level.ServerPlayer
import us.timinc.mc.cobblemon.droploottables.DropLootTables.modIdentifier
import us.timinc.mc.cobblemon.droploottables.extensions.getPokedexManager

object DropLootTablesEventHandlers {
    val PRE_CATCH_PLAYER_KNOWLEDGE = modIdentifier("already_registered")

    fun register() {
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST) { it.cancel() }
        CobblemonEvents.POKE_BALL_CAPTURE_CALCULATED.subscribe(Priority.HIGHEST) { event ->
            val player = event.thrower as? ServerPlayer ?: return@subscribe
            val pokemon = event.pokemonEntity.pokemon
            pokemon.persistentData.putString(
                PRE_CATCH_PLAYER_KNOWLEDGE.toString(),
                (player.getPokedexManager().getSpeciesRecord(pokemon.species.resourceIdentifier)
                    ?.getFormRecord(pokemon.form.name)?.knowledge ?: PokedexEntryProgress.NONE).name
            )
        }
    }
}