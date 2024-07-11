package us.timinc.mc.cobblemon.droploottables

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.api.events.CobblemonEvents
import com.cobblemon.mod.common.entity.pokemon.PokemonEntity
import com.cobblemon.mod.common.util.server
import net.fabricmc.api.ModInitializer
import net.minecraft.loot.LootDataType
import net.minecraft.loot.context.LootContextParameterSet
import net.minecraft.loot.context.LootContextParameters
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.Identifier
import net.minecraft.util.math.Vec3d
import us.timinc.mc.cobblemon.droploottables.implementation.cobblemon.DynamicItemDropEntry

object DropLootTables : ModInitializer {
    @Suppress("MemberVisibilityCanBePrivate")
    const val MOD_ID = "droploottables"

    override fun onInitialize() {
        CobblemonEvents.LOOT_DROPPED.subscribe(Priority.LOWEST) { event ->
            val world = event.player?.world ?: event.entity?.world ?: server()?.overworld ?: return@subscribe
            if (world.server == null) return@subscribe

            val dropperEntity = event.entity ?: return@subscribe
            if (dropperEntity !is PokemonEntity) return@subscribe

            val position = dropperEntity.blockPos
            val vecPos = Vec3d(position.x.toDouble(), position.y.toDouble(), position.z.toDouble())

            val pokemon = dropperEntity.pokemon

            val identifier =
                Identifier(MOD_ID, "gameplay/pokedrops/${pokemon.species.resourceIdentifier.path}")
            val lootManager = world.server!!.lootManager

            if (!lootManager.getIds(LootDataType.LOOT_TABLES).contains(identifier)) {
                return@subscribe
            }

            val lootTable = lootManager.getLootTable(identifier)
            val list = lootTable.generateLoot(
                LootContextParameterSet(
                    world as ServerWorld,
                    mapOf(
                        LootContextParameters.ORIGIN to vecPos
                    ),
                    mapOf(),
                    0F
                )
            )

            event.drops.clear()
            event.drops.addAll(list.map(::DynamicItemDropEntry))
        }
    }
}