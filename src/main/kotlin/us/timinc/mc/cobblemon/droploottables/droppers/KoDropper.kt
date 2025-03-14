package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.drop.ItemDropEntry
import net.minecraft.core.registries.Registries
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.config
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.events.DropLootTablesEvents
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object KoDropper : AbstractFormDropper("ko") {
    override fun load() {
        DropLootTablesEvents.WILD_POKEMON_FAINTED.subscribe(Priority.LOWEST) { event ->
            val pokemonEntity = event.pokemon.entity ?: return@subscribe
            val level = pokemonEntity.level()
            if (level !is ServerLevel) return@subscribe
            val player = pokemonEntity.killer
            val position = pokemonEntity.position()
            val wasInBattle = pokemonEntity.isBattling
            val attacker = pokemonEntity.lastAttacker
            val directAttackingEntity = pokemonEntity.lastDamageSource
            val attackingPlayer = attacker as? ServerPlayer
            val pokemon = event.pokemon
            val form = pokemon.form

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.WAS_IN_BATTLE to wasInBattle,
                    LootConditions.PARAMS.RELEVANT_PLAYER to player,
                    LootContextParams.ATTACKING_ENTITY to attacker,
                    LootContextParams.DIRECT_ATTACKING_ENTITY to directAttackingEntity,
                    LootContextParams.LAST_DAMAGE_PLAYER to attackingPlayer
                ),
                mapOf(),
                player?.luck ?: 0F
            )
            val finalDrops: MutableList<ItemStack> = mutableListOf()
            val tableDrops = getDrops(
                lootParams,
                FormDropContext(form)
            )
            finalDrops.addAll(tableDrops)

            if (!lootTableExists(level, getFormDropId(form)) && config.useCobblemonDropsIfOverrideNotPresent) {
                val cobbleDrops = form.drops.getDrops(pokemon = event.pokemon)
                finalDrops.addAll(cobbleDrops.mapNotNull { drop ->
                    if (drop is ItemDropEntry) {
                        val item = level.registryAccess().registryOrThrow(Registries.ITEM).get(drop.item)
                        if (item === null) {
                            debug("Unable to drop item ${drop.item}", true)
                            return@mapNotNull null
                        }
                        return@mapNotNull ItemStack(item, drop.quantityRange?.random() ?: drop.quantity)
                    }
                    drop.drop(null, level, position, player)
                    return@mapNotNull null
                })
            }

            player?.let { giveDropsToPlayer(finalDrops, it) } ?: finalDrops.forEach { drop ->
                level.addFreshEntity(ItemEntity(level, pokemonEntity.x, pokemonEntity.y, pokemonEntity.z, drop))
            }
        }
    }
}