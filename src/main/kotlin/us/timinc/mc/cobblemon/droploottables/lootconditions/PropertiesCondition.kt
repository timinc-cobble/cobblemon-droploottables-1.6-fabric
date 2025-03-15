package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokemon.PokemonProperties
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.DropLootTables.debug
import us.timinc.mc.cobblemon.droploottables.extensions.isInvalid

class PropertiesCondition(
    val properties: String,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val PROPERTIES = "properties"
        }

        val CODEC: MapCodec<PropertiesCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.PROPERTIES).forGetter { it.properties }
            ).apply(instance, ::PropertiesCondition)
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        val properties: PokemonProperties = PokemonProperties.parse(properties)
        return properties.matches(pokemon)
    }

    override fun getType(): LootItemConditionType = LootConditions.PROPERTIES
}