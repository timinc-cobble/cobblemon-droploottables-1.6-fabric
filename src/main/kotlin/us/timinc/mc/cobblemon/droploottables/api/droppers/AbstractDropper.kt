package us.timinc.mc.cobblemon.droploottables.api.droppers

import com.cobblemon.mod.common.pokemon.FormData
import com.cobblemon.mod.common.util.giveOrDropItemStack
import net.minecraft.core.registries.Registries
import net.minecraft.resources.ResourceKey
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.DropLootTables.modIdentifier

abstract class AbstractDropper(
    open val dropType: String,
) {
    abstract fun load()

    open fun lootTableExists(level: ServerLevel, tableId: ResourceLocation) =
        level.server.reloadableRegistries().getKeys(Registries.LOOT_TABLE).contains(tableId)

    open fun getFormDropId(form: FormData) =
        modIdentifier("$dropType/${form.species.resourceIdentifier.path}${if (form.name != "Normal") "/${form.name.lowercase()}" else ""}")

    open fun getAllDropId() =
        modIdentifier("$dropType/all")

    open fun getDrops(params: LootParams, form: FormData): List<ItemStack> {
        val results: MutableList<ItemStack> = mutableListOf()

        results.addAll(getDropsFromTable(params.level, getFormDropId(form), params))
        results.addAll(getDropsFromTable(params.level, getAllDropId(), params))

        debug("Drop results ($dropType): $results")

        return results
    }

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