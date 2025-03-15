package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.brigadier.StringReader
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.commands.arguments.item.ItemParser
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class HeldItemCondition(
    val item: String,
): LootItemCondition {
    companion object {
        object KEYS {
            const val ITEM = "item"
        }

        val CODEC: MapCodec<HeldItemCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.ITEM).forGetter(HeldItemCondition::item)
            ).apply(instance, ::HeldItemCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val heldItem = pokemon.heldItem()
        if (heldItem.isEmpty) return false
        val parser = ItemParser(context.level.server.registryAccess())
        val result = parser.parse(StringReader(item))

        if (!heldItem.`is`(result.item)) return false

        for (entry in result.components.entrySet()) {
            val targetPropValue = heldItem.get(entry.key)
            if (targetPropValue != entry.value.get()) {
                return false
            }
        }

        return true
    }

    override fun getType(): LootItemConditionType = LootConditions.HELD_ITEM
}