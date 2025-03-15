package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class LabelCondition(
    val labels: List<String>,
    val all: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val LABELS = "labels"
            const val ALL = "all"
        }

        val CODEC: MapCodec<LabelCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.LABELS).forGetter(LabelCondition::labels),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(LabelCondition::all)
            ).apply(instance, ::LabelCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonLabels = pokemon.form.labels
        return if (all) labels.all(pokemonLabels::contains) else labels.any(pokemonLabels::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.LABEL
}