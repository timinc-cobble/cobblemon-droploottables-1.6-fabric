package us.timinc.mc.cobblemon.droploottables.api.droppers

import com.cobblemon.mod.common.util.giveOrDropItemStack
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug

abstract class AbstractDropper<T : DropContext>(open val dropType: String, open val modId: String) {
    abstract fun load()

    abstract fun getDrops(params: LootParams, context: T): List<ItemStack>

    open fun lootTableExists(level: ServerLevel, tableId: ResourceLocation) =
        level.server.reloadableRegistries().getKeys(Registries.LOOT_TABLE).contains(tableId)

    open fun getDropsFromTable(level: ServerLevel, tableId: ResourceLocation, params: LootParams): List<ItemStack> {
        if (!lootTableExists(level, tableId)) {
            debug("No table present for $tableId")
            return emptyList()
        }

        val lootTable = level.server.reloadableRegistries().getLootTable(
            ResourceKey.create(Registries.LOOT_TABLE, tableId)
        )

        val results = lootTable.getRandomItems(
            params
        )
        debug("Table $tableId dropResults: $results")
        return results
    }

    open fun giveDropsToPlayer(drops: List<ItemStack>, player: ServerPlayer) {
        drops.forEach(player::giveOrDropItemStack)
    }
}