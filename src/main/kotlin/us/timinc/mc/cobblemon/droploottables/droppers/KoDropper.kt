package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.dropentries.DynamicItemDropEntry
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object KoDropper : AbstractFormDropper("ko") {
    override fun load() {
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.LOWEST) { event ->

            val level = event.player?.level() ?: event.entity?.level() ?: return@subscribe
            if (level !is ServerLevel) return@subscribe
            val dropperEntity = event.entity ?: return@subscribe
            if (dropperEntity !is PokemonEntity) return@subscribe
            val wasKilledInBattle = dropperEntity.isBattling
            val playerEntity = (if (wasKilledInBattle) dropperEntity.battleId?.let {
                Cobblemon.battleRegistry.getBattle(
                    it
                )
            }?.players?.first() else event.player) ?: return@subscribe
            val form = dropperEntity.pokemon.form

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to dropperEntity.position(),
                    LootContextParams.THIS_ENTITY to dropperEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to dropperEntity.pokemon,
                    LootConditions.PARAMS.WAS_IN_BATTLE to wasKilledInBattle,
                    LootContextParams.DIRECT_ATTACKING_ENTITY to playerEntity,
                    LootContextParams.LAST_DAMAGE_PLAYER to dropperEntity.lastAttacker,
                ),
                mapOf(),
                event.player?.luck ?: 0F
            )
            val drops = getDrops(
                lootParams,
                FormDropContext(form)
            )

            if (lootTableExists(level, getFormDropId(form))) {
                event.drops.clear()
            }
            event.drops.addAll(drops.map(::DynamicItemDropEntry))
        }
    }
}