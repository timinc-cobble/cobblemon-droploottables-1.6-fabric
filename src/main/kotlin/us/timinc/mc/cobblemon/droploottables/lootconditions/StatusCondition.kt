package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.parseWithDefaultedCobblemonNamespace

class StatusCondition(
    val statuses: List<ResourceLocation>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val STATUS = "status"
        }

        val CODEC: MapCodec<StatusCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.STATUS).forGetter { it.statuses.map(ResourceLocation::toString) },
            ).apply(instance) { StatusCondition(it.map(::parseWithDefaultedCobblemonNamespace)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val pokemonStatus = pokemon.status ?: return false
        return statuses.contains(pokemonStatus.status.name)
    }

    override fun getType(): LootItemConditionType = LootConditions.STATUS
}