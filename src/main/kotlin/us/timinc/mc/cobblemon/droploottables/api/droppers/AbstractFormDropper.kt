package us.timinc.mc.cobblemon.droploottables.api.droppers

import com.cobblemon.mod.common.pokemon.FormData
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.storage.loot.LootParams
import us.timinc.mc.cobblemon.droploottables.DropLootTables.MOD_ID
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug

abstract class AbstractFormDropper(dropType: String, modId: String = MOD_ID) :
    AbstractDropper<FormDropContext>(dropType, modId) {
    open fun getFormDropId(form: FormData): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(
            modId,
            "$dropType/${form.species.resourceIdentifier.path}${
                if (form.name != "Normal") "/${
                    form.name.lowercase().replace(Regex("[^a-z0-9/._-]"), "")
                }" else ""
            }"
        )

    open fun getAllDropId(): ResourceLocation =
        ResourceLocation.fromNamespaceAndPath(modId, "$dropType/all")

    override fun getDrops(params: LootParams, context: FormDropContext): List<ItemStack> {
        val results: MutableList<ItemStack> = mutableListOf()

        results.addAll(getDropsFromTable(params.level, getFormDropId(context.formData), params))
        results.addAll(getDropsFromTable(params.level, getAllDropId(), params))

        debug("Drop results ($dropType): $results")

        return results
    }
}