package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.ResourceLocation
import us.timinc.mc.cobblemon.droploottables.config.ConfigBuilder
import us.timinc.mc.cobblemon.droploottables.config.MainConfig
import us.timinc.mc.cobblemon.droploottables.droppers.Droppers
import us.timinc.mc.cobblemon.droploottables.events.DropLootTablesEventHandlers
import us.timinc.mc.cobblemon.droploottables.events.DropLootTablesEvents
import us.timinc.mc.cobblemon.droploottables.events.PokemonEntityTickedEvent
import us.timinc.mc.cobblemon.droploottables.lootconditions.LootConditions
import us.timinc.mc.cobblemon.droploottables.lootconditions.counter.CounterLootConditions

object DropLootTables : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    const val MOD_ID = "droploottables"
    lateinit var config: MainConfig

    override fun onInitialize() {
        config = ConfigBuilder.load(MainConfig::class.java, MOD_ID)
        LootConditions.register()
        Droppers.load()
        DropLootTablesEventHandlers.register()
    }

    fun initializeCounter() {
        CounterLootConditions.register()
    }

    fun modIdentifier(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(MOD_ID, name)
    }

    fun debug(msg: String, bypassConfig: Boolean = false) {
        if (!config.debug && !bypassConfig) {
            return
        }
        println(msg)
    }

    fun pokemonEntityTicked(entity: PokemonEntity) {
        DropLootTablesEvents.POKEMON_TICKED.post(PokemonEntityTickedEvent(entity))
    }
}