package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class NicknameCondition(
    val name: String,
    val caseInsensitive: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val NAME = "name"
            const val CASE_INSENSITIVE = "case_insensitive"
        }

        val CODEC: MapCodec<NicknameCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.NAME).forGetter(NicknameCondition::name),
                Codec.BOOL.fieldOf(KEYS.CASE_INSENSITIVE).orElse(false).forGetter(NicknameCondition::caseInsensitive)
            ).apply(instance, ::NicknameCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val nickname = pokemon.nickname ?: return false
        return if (caseInsensitive) nickname.string.lowercase() == name.lowercase() else nickname.string == name
    }

    override fun getType(): LootItemConditionType = LootConditions.NICKNAME
}