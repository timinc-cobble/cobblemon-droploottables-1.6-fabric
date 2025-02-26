package us.timinc.mc.cobblemon.droploottables.lootconditions.counter

import net.minecraft.core.Registry
import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
import us.timinc.mc.cobblemon.droploottables.DropLootTables

object CounterLootConditions {
    val COUNT_CONDITION: LootItemConditionType = LootItemConditionType(CounterCondition.CODEC)

    fun register() {
        Registry.register(
            BuiltInRegistries.LOOT_CONDITION_TYPE, DropLootTables.modIdentifier("counter_count"), COUNT_CONDITION
        )
    }
}