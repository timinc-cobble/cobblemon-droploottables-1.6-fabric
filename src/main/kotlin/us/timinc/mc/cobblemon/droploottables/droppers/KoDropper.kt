package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.util.giveOrDropItemStack
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object KoDropper : AbstractFormDropper("ko") {
    override fun load() {
        CobblemonEvents.POKEMON_FAINTED.subscribe(Priority.LOWEST) { event ->
            if (!event.pokemon.isWild()) return@subscribe

            val dropperEntity = event.pokemon.entity ?: return@subscribe
            val player = dropperEntity.killer
            val level = dropperEntity.level()
            if (level !is ServerLevel) return@subscribe
            val position = dropperEntity.position()
            val wasInBattle = dropperEntity.isBattling
            val attacker = dropperEntity.lastAttacker
            val directAttackingEntity = dropperEntity.lastDamageSource
            val attackingPlayer = if (attacker is ServerPlayer) attacker else null

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to dropperEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to event.pokemon,
                    LootConditions.PARAMS.WAS_IN_BATTLE to wasInBattle,
                    LootContextParams.ATTACKING_ENTITY to attacker,
                    LootContextParams.DIRECT_ATTACKING_ENTITY to directAttackingEntity,
                    LootContextParams.LAST_DAMAGE_PLAYER to attackingPlayer
                ),
                mapOf(),
                player?.luck ?: 0F
            )
            val drops = getDrops(
                lootParams,
                FormDropContext(event.pokemon.form)
            )

            player?.let { giveDropsToPlayer(drops, it) } ?: drops.forEach { drop ->
                level.addFreshEntity(ItemEntity(level, dropperEntity.x, dropperEntity.y, dropperEntity.z, drop))
            }
        }
    }
}