package us.timinc.mc.cobblemon.droploottables.config

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.pokemon.Pokemon
import us.timinc.mc.cobblemon.droploottables.toIntRange

class MainConfig {
    var debug: Boolean = false
    var useCobblemonDropsIfOverrideNotPresent = true
    var granularDropPeriods: Map<String, String> = mapOf()

    fun getGranularDropPeriodFor(pokemon: Pokemon): IntRange? =
        granularDropPeriods.entries.find { (k) ->
            PokemonProperties.parse(k).matches(pokemon)
        }?.value?.let(::toIntRange)
}