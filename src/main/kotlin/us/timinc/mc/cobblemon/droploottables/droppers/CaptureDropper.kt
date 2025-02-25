package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractDropper
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object CaptureDropper : AbstractDropper("capture") {
    override fun load() {
        CobblemonEvents.POKEMON_CAPTURED.subscribe { event ->
            val level = event.pokemon.entity?.level() ?: event.player.level()
            if (level !is ServerLevel) return@subscribe
            val dropperEntity = event.player

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to dropperEntity.position(),
                    LootContextParams.THIS_ENTITY to dropperEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to event.pokemon,
                    LootContextParams.DIRECT_ATTACKING_ENTITY to event.player
                ),
                mapOf(),
                event.player.luck
            )
            val drops = getDrops(
                lootParams,
                event.pokemon.form
            )

            giveDropsToPlayer(drops, event.player)
        }
    }
}