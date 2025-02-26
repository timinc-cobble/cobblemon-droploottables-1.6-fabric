package us.timinc.mc.cobblemon.droploottables.api.droppers

import com.cobblemon.mod.common.pokemon.FormData
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.DropLootTables.modIdentifier

abstract class AbstractFormDropper(dropType: String) : AbstractDropper<FormDropContext>(dropType) {
    open fun getFormDropId(form: FormData) =
        modIdentifier("$dropType/${form.species.resourceIdentifier.path}${if (form.name != "Normal") "/${form.name.lowercase()}" else ""}")

    open fun getAllDropId() =
        modIdentifier("$dropType/all")

    override fun getDrops(params: LootParams, context: FormDropContext): List<ItemStack> {
        val results: MutableList<ItemStack> = mutableListOf()

        results.addAll(getDropsFromTable(params.level, getFormDropId(context.formData), params))
        results.addAll(getDropsFromTable(params.level, getAllDropId(), params))

        debug("Drop results ($dropType): $results")

        return results
    }
}