@file:Suppress("MemberVisibilityCanBePrivate")

package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.pokemon.Pokemon
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.config
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.events.DropLootTablesEvents
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object PeriodicDropper : AbstractFormDropper("periodic") {
    const val PERSISTENT_DATA_KEY_TIMER = "droploottables:periodic_timer"

    override fun load() {
        DropLootTablesEvents.POKEMON_TICKED.subscribe { event ->
            val pokemonEntity = event.entity
            val pokemon = pokemonEntity.pokemon
            val level = pokemonEntity.level()
            if (level !is ServerLevel) return@subscribe
            if (!isReady(pokemon)) return@subscribe
            val position = pokemonEntity.position()

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon
                ),
                mapOf(),
                0F
            )
            val context = FormDropContext(pokemon.form)
            val drops = getDrops(
                lootParams,
                context
            )

            drops.forEach { drop ->
                level.addFreshEntity(ItemEntity(level, pokemonEntity.x, pokemonEntity.y, pokemonEntity.z, drop))
            }
        }
    }

    private fun getDropTimer(pokemon: Pokemon): Int {
        val range = config.getGranularDropPeriodFor(pokemon) ?: IntRange(6000, 12000)
        debug("Got range $range from ${pokemon.species}")
        return range.random()
    }

    private fun isReady(pokemon: Pokemon): Boolean {
        if (!pokemon.persistentData.contains(PERSISTENT_DATA_KEY_TIMER)) {
            pokemon.persistentData.putInt(PERSISTENT_DATA_KEY_TIMER, getDropTimer(pokemon))
            return false
        }
        val currentValue = pokemon.persistentData.getInt(PERSISTENT_DATA_KEY_TIMER)
        if (currentValue <= 0) {
            pokemon.persistentData.putInt(PERSISTENT_DATA_KEY_TIMER, getDropTimer(pokemon))
            return true
        }
        pokemon.persistentData.putInt(PERSISTENT_DATA_KEY_TIMER, currentValue - 1)
        return false
    }
}