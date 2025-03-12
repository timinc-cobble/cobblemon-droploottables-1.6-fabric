package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.pokemon.OriginalTrainerType
import com.cobblemon.mod.common.pokemon.Pokemon

fun toIntRange(str: String): IntRange {
    val (start, end) = str.split("..")

    return try {
        start.toInt()..end.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("'$start' and/or '$end' is/are not integers", e)
    }
}

// This is here because the idea of an NPC owning a Pokemon isn't really well known yet. RCT in particular is causing
// pokemon::isWild to return true even though it has an NPC trainer. So, double-check for the OT; wilds should never
// have OTs is hopefully a sound idea.
fun isActuallyWild(pokemon: Pokemon): Boolean = pokemon.isWild() && pokemon.originalTrainerType == OriginalTrainerType.NONE