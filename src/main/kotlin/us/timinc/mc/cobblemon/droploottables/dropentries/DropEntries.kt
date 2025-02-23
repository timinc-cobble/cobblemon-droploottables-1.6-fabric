package us.timinc.mc.cobblemon.droploottables.dropentries

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.drop.ItemDropMethod
import com.cobblemon.mod.common.api.text.green
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.util.lang
import com.cobblemon.mod.common.util.toBlockPos
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.LivingEntity
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.block.Blocks
import net.minecraft.world.phys.Vec3

open class DynamicItemDropEntry(
    open val stack: ItemStack,
) : DropEntry {
    override val quantity: Int = 1
    override val maxSelectableTimes: Int = 1
    override val percentage: Float = 100f

    override fun drop(entity: LivingEntity?, world: ServerLevel, pos: Vec3, player: ServerPlayer?) {
        val inLava = world.getBlockState(pos.toBlockPos()).block == Blocks.LAVA
        val dropMethod = (Cobblemon.config.defaultDropItemMethod).let {
            if (inLava) {
                ItemDropMethod.TO_INVENTORY
            } else {
                it
            }
        }

        if (dropMethod == ItemDropMethod.ON_PLAYER && player != null) {
            world.addFreshEntity(ItemEntity(player.level(), player.x, player.y, player.z, stack))
        } else if (dropMethod == ItemDropMethod.TO_INVENTORY && player != null) {
            val name = stack.displayName
            val count = stack.count
            val succeeded = player.addItem(stack)
            if (Cobblemon.config.announceDropItems) {
                player.sendSystemMessage(
                    if (succeeded) lang("drop.item.inventory", count, name.copy().green())
                    else lang("drop.item.full", name).red()
                )
            }
        } else if (dropMethod == ItemDropMethod.ON_ENTITY && entity != null) {
            world.addFreshEntity(ItemEntity(entity.level(), entity.x, entity.y, entity.z, stack))
        } else {
            world.addFreshEntity(ItemEntity(world, pos.x, pos.y, pos.z, stack))
        }
    }
}