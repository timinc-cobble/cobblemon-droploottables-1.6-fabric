package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.pokemon.OriginalTrainerType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.math.FloatRange

fun toIntRange(str: String): IntRange {
    val (start, end) = str.split("..")

    return try {
        val actualStart = when (start.lowercase()) {
            "min" -> Int.MIN_VALUE
            else -> start.toInt()
        }
        val actualEnd = when (end.lowercase()) {
            "max" -> Int.MAX_VALUE
            else -> end.toInt()
        }
        actualStart..actualEnd
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("'$start' and/or '$end' is/are not integers", e)
    }
}

fun toFloatRange(str: String): FloatRange {
    val (start, end) = str.split("..")

    return try {
        val actualStart = when (start.lowercase()) {
            "min" -> Float.MIN_VALUE
            else -> start.toFloat()
        }
        val actualEnd = when (end.lowercase()) {
            "max" -> Float.MAX_VALUE
            else -> end.toFloat()
        }
        FloatRange(actualStart, actualEnd)
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("'$start' and/or '$end' is/are not Floats", e)
    }
}

// This is here because the idea of an NPC owning a Pokemon isn't really well known yet. RCT in particular is causing
// pokemon::isWild to return true even though it has an NPC trainer. So, double-check for the OT; wilds should never
// have OTs is hopefully a sound idea.
fun isActuallyWild(pokemon: Pokemon): Boolean =
    pokemon.isWild() && pokemon.originalTrainerType == OriginalTrainerType.NONE