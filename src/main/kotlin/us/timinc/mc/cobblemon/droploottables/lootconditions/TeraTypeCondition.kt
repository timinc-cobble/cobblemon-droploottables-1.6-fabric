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

class TeraTypeCondition(
    val types: List<ResourceLocation>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val TERA_TYPE = "type"
        }

        val CODEC: MapCodec<TeraTypeCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.TERA_TYPE).forGetter { it.types.map(ResourceLocation::toString) }
            ).apply(instance) { TeraTypeCondition(it.map(::parseWithDefaultedCobblemonNamespace)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return types.contains(pokemon.teraType.id)
    }

    override fun getType(): LootItemConditionType = LootConditions.TERA_TYPE
}