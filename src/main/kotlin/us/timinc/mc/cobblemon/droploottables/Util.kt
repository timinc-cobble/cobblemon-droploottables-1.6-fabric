package us.timinc.mc.cobblemon.droploottables

fun toIntRange(str: String): IntRange {
    val (start, end) = str.split("..")

    return try {
        start.toInt()..end.toInt()
    } catch (e: NumberFormatException) {
        throw IllegalArgumentException("'$start' and/or '$end' is/are not integers", e)
    }
}