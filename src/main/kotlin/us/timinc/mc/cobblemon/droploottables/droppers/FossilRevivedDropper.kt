package us.timinc.mc.cobblemon.droploottables.droppers

import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.server
import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.level.storage.loot.LootParams
import net.minecraft.world.level.storage.loot.parameters.LootContextParams
import us.timinc.mc.cobblemon.droploottables.api.droppers.AbstractFormDropper
import us.timinc.mc.cobblemon.droploottables.api.droppers.FormDropContext
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions

object FossilRevivedDropper : AbstractFormDropper("resurrect") {
    override fun load() {
        CobblemonEvents.FOSSIL_REVIVED.subscribe { event ->
            val pokemon = event.pokemon
            val player = event.player
            val pokemonEntity = pokemon.entity ?: findPokemonEntity(pokemon)
            val positionalEntity = player ?: pokemonEntity ?: return@subscribe
            val level = positionalEntity.level()
            if (level !is ServerLevel) return@subscribe
            val position = positionalEntity.position()
            val form = pokemon.form

            val lootParams = LootParams(
                level,
                mapOf(
                    LootContextParams.ORIGIN to position,
                    LootContextParams.THIS_ENTITY to pokemonEntity,
                    LootConditions.PARAMS.POKEMON_DETAILS to pokemon,
                    LootConditions.PARAMS.RELEVANT_PLAYER to player,
                ),
                mapOf(),
                player?.luck ?: 0F
            )
            val context = FormDropContext(form)
            val drops = getDrops(
                lootParams,
                context
            )

            player?.let { giveDropsToPlayer(drops, it) } ?: drops.forEach { drop ->
                level.addFreshEntity(ItemEntity(level, pokemonEntity!!.x, pokemonEntity.y, pokemonEntity.z, drop))
            }
        }
    }

    private fun findPokemonEntity(pokemon: Pokemon): PokemonEntity? {
        val server = server() ?: return null
        server.allLevels.forEach { level ->
            return level.allEntities.firstOrNull { it is PokemonEntity && it.pokemon == pokemon } as? PokemonEntity
        }
        return null
    }
}