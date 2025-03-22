package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.util.giveOrDropItemStack
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object ReleaseDropper : AbstractFormDropper("release") {
    override fun load() {
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val level = event.player.level()
            if (level !is ServerLevel) return@subscribe
            val player = event.player
            val position = player.position()
            val pokemon = event.pokemon

            val lootParams = LootParams(
                level, mapOf(
                    LootContextParams.ORIGIN to position,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.RELEVANT_PLAYER to player,
                ), mapOf(), player.luck
            )
            val context = FormDropContext(pokemon.form)
            val drops = getDrops(
                lootParams,
                context
            )

            drops.forEach(event.player::giveOrDropItemStack)
        }
    }
}