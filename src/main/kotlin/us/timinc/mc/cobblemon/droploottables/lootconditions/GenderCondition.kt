package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class GenderCondition(
    val genders: List<String>
): LootItemCondition {
    companion object {
        object KEYS {
            const val GENDERS = "genders"
        }

        val CODEC: MapCodec<GenderCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.GENDERS).forGetter(GenderCondition::genders)
            ).apply(instance, ::GenderCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonGender = pokemon.gender.name
        return genders.contains(pokemonGender.lowercase())
    }

    override fun getType(): LootItemConditionType = LootConditions.GENDER
}