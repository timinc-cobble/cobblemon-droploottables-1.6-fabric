package us.timinc.mc.cobblemon.droploottables.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.reactive.EventObservable
import com.cobblemon.mod.common.api.reactive.Observable.Companion.filter

object DropLootTablesEvents {
    @JvmField
    val POKEMON_TICKED = EventObservable<PokemonEntityTickedEvent>()

    @JvmField
    val WILD_POKEMON_TICKED = POKEMON_TICKED
        .pipe(
            filter { it.entity.pokemon.isWild() }
        )

    @JvmField
    val WILD_POKEMON_FAINTED = CobblemonEvents.POKEMON_FAINTED
        .pipe(
            filter { it.pokemon.isWild() }
        )
}