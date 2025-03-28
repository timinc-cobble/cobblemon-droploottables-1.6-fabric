package us.timinc.mc.cobblemon.droploottables.lootconditions

import com.cobblemon.mod.common.api.pokedex.PokedexEntryProgress
import com.cobblemon.mod.common.pokemon.Pokemon
import com.mojang.serialization.Codec
import com.mojang.serialization.MapCodec
import com.mojang.serialization.codecs.RecordCodecBuilder
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.level.storage.loot.LootContext
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.events.DropLootTablesEventHandlers
import us.timinc.mc.cobblemon.droploottables.extensions.getPokedexManager

class KnowledgeLevelCondition(
    val knowledge: PokedexEntryProgress,
) : LootItemCondition {
    companion object {
        object KEYS {
            const val KNOWLEDGE = "knowledge"
        }

        val CODEC: MapCodec<KnowledgeLevelCondition> = RecordCodecBuilder.mapCodec { instance ->
            instance.group(
                Codec.STRING.fieldOf(KEYS.KNOWLEDGE).forGetter { it.knowledge.name }
            ).apply(instance) { k -> KnowledgeLevelCondition(PokedexEntryProgress.valueOf(k.uppercase())) }
        }
    }

    override fun test(context: LootContext): Boolean {
        val pokemon: Pokemon = context.getParamOrNull(LootConditions.PARAMS.POKEMON_DETAILS) ?: return false
        if (pokemon.persistentData.contains(DropLootTablesEventHandlers.PRE_CATCH_PLAYER_KNOWLEDGE.toString())) {
            val storedKnowledgeLevelName =
                pokemon.persistentData.getString(DropLootTablesEventHandlers.PRE_CATCH_PLAYER_KNOWLEDGE.toString())
            val storedKnowledgeLevel = PokedexEntryProgress.valueOf(storedKnowledgeLevelName)
            return storedKnowledgeLevel.ordinal >= knowledge.ordinal
        }
        val player: ServerPlayer = context.getParamOrNull(LootConditions.PARAMS.RELEVANT_PLAYER) ?: return false
        val knowledgeLevel = player.getPokedexManager().getSpeciesRecord(pokemon.species.resourceIdentifier)?.getFormRecord(pokemon.form.name)?.knowledge ?: PokedexEntryProgress.NONE
        return knowledgeLevel.ordinal >= knowledge.ordinal
    }

    override fun getType(): LootItemConditionType = LootConditions.KNOWLEDGE_LEVEL
}