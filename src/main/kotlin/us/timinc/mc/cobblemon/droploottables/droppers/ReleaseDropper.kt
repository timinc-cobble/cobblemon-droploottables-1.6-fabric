package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.util.giveOrDropItemStack
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractDropper
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object ReleaseDropper : AbstractDropper("release") {
    override fun load() {
        CobblemonEvents.POKEMON_RELEASED_EVENT_POST.subscribe(Priority.LOWEST) { event ->
            val level = event.player.level()
            if (level !is ServerLevel) return@subscribe

            val lootParams = LootParams(
                level, mapOf(
                    LootContextParams.ORIGIN to event.player.position(),
                    LootContextParams.THIS_ENTITY to event.pokemon.entity,
                    LootConditions.PARAMS.POKEMON_DETAILS to event.pokemon,
                    LootContextParams.DIRECT_ATTACKING_ENTITY to event.player
                ), mapOf(), event.player.luck
            )
            val drops = getDrops(
                lootParams, event.pokemon.form
            )

            drops.forEach(event.player::giveOrDropItemStack)
        }
    }
}