package us.timinc.mc.cobblemon.droploottables.events

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents

object DropLootTablesEventHandlers {
    fun register() {
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.HIGHEST) { it.cancel() }
    }
}