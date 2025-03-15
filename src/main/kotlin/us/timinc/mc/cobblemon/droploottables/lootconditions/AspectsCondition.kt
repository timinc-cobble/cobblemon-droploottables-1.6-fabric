package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class AspectsCondition(
    val aspects: List<String>,
    val all: Boolean = false
) : LootItemCondition {
    companion object {
        object KEYS {
            const val ASPECTS = "aspects"
            const val ALL = "all"
        }

        val CODEC: MapCodec<AspectsCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.ASPECTS).forGetter(AspectsCondition::aspects),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(AspectsCondition::all),
            ).apply(instance, ::AspectsCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return if (all) aspects.all(pokemon.aspects::contains) else aspects.any(pokemon.aspects::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.ASPECTS
}