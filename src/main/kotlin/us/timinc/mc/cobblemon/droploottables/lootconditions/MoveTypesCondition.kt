package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.types.ElementalType
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class MoveTypesCondition(
    val moveTypes: List<ElementalType>,
    val all: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val MOVE_TYPES = "move_types"
            const val ALL = "all"
        }

        val CODEC: MapCodec<MoveTypesCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                ElementalType.BY_STRING_CODEC.listOf().fieldOf(KEYS.MOVE_TYPES)
                    .forGetter(MoveTypesCondition::moveTypes),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(MoveTypesCondition::all)
            ).apply(instance, ::MoveTypesCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonMoveNames = pokemon.moveSet.map { it.type }
        return if (all) moveTypes.all(pokemonMoveNames::contains) else moveTypes.any(pokemonMoveNames::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.MOVES
}