package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class MovesCondition(
    val moves: List<String>,
    val all: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val MOVES = "moves"
            const val ALL = "all"
        }

        val CODEC: MapCodec<MovesCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.MOVES).forGetter(MovesCondition::moves),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(MovesCondition::all)
            ).apply(instance, ::MovesCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonMoveNames = pokemon.moveSet.map { it.name }
        return if (all) moves.all(pokemonMoveNames::contains) else moves.any(pokemonMoveNames::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.MOVES
}