package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.egg.EggGroup
import com.cobblemon.mod.common.pokemon.Pokemon
import com.cobblemon.mod.common.util.codec.CodecUtils
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType

class EggGroupCondition(
    val eggGroups: List<EggGroup>,
    val all: Boolean = false,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val EGG_GROUPS = "egg_groups"
            const val ALL = "all"
        }

        // TODO: Remove if/when Cobblemon adds one.
        private val EGG_GROUP_BY_STRING_CODEC: Codec<EggGroup> = CodecUtils.createByStringCodec(
            EggGroup::fromIdentifier,
            EggGroup::name
        ) { id -> "No EggGroup for ID $id" }

        val CODEC: MapCodec<EggGroupCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                EGG_GROUP_BY_STRING_CODEC.listOf().fieldOf(KEYS.EGG_GROUPS).forGetter(EggGroupCondition::eggGroups),
                Codec.BOOL.fieldOf(KEYS.ALL).orElse(false).forGetter(EggGroupCondition::all)
            ).apply(instance, ::EggGroupCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonEggGroups = pokemon.form.eggGroups
        return if (all) eggGroups.all(pokemonEggGroups::contains) else eggGroups.any(pokemonEggGroups::contains)
    }

    override fun getType(): LootItemConditionType = LootConditions.EGG_GROUP
}