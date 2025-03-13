package us.timinc.mc.cobblemon.droploottables.events

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.api.reactive.EventObservable
import com.cobblemon.mod.common.api.reactive.Observable.Companion.filter
import us.timinc.mc.cobblemon.droploottables.isActuallyWild

object DropLootTablesEvents {
    @JvmField
    val POKEMON_TICKED = EventObservable<PokemonEntityTickedEvent>()

    @JvmField
    val WILD_POKEMON_TICKED = POKEMON_TICKED
        .pipe(
            filter { isActuallyWild(it.entity.pokemon) }
        )

    @JvmField
    val WILD_POKEMON_FAINTED = CobblemonEvents.POKEMON_FAINTED
        .pipe(
            filter { isActuallyWild(it.pokemon) }
        )
}