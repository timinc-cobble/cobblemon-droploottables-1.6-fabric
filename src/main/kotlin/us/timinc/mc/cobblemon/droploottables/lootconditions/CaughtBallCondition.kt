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

class CaughtBallCondition(
    val balls: List<ResourceLocation>,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val BALLS = "balls"
        }

        val CODEC: MapCodec<CaughtBallCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.listOf().fieldOf(KEYS.BALLS).forGetter { it.balls.map(ResourceLocation::toString) }
            ).apply(instance) { CaughtBallCondition(it.map(::parseWithDefaultedCobblemonNamespace)) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        return balls.contains(pokemon.caughtBall.name)
    }

    override fun getType(): LootItemConditionType = LootConditions.CAUGHT_BALL
}