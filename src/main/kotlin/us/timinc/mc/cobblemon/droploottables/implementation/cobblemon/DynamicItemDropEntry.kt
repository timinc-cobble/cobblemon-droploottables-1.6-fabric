package us.timinc.mc.cobblemon.droploottables.implementation.cobblemon

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.drop.DropEntry
import com.cobblemon.mod.common.api.drop.ItemDropMethod
import com.cobblemon.mod.common.api.text.green
import com.cobblemon.mod.common.api.text.red
import com.cobblemon.mod.common.util.lang
import com.cobblemon.mod.common.util.toBlockPos
import net.minecraft.block.Blocks
import net.minecraft.entity.ItemEntity
import net.minecraft.entity.LivingEntity
import net.minecraft.item.ItemStack
import net.minecraft.server.network.ServerPlayerEntity
import net.minecraft.server.world.ServerWorld
import net.minecraft.util.math.Vec3d

open class DynamicItemDropEntry(
    open val stack: ItemStack
) : DropEntry {
    override val quantity: Int = 1
    override val maxSelectableTimes: Int = 1
    override val percentage: Float = 100f

    override fun drop(entity: LivingEntity?, world: ServerWorld, pos: Vec3d, player: ServerPlayerEntity?) {
        val inLava = world.getBlockState(pos.toBlockPos()).block == Blocks.LAVA
        val dropMethod = (Cobblemon.config.defaultDropItemMethod).let {
            if (inLava) {
                ItemDropMethod.TO_INVENTORY
            } else {
                it
            }
        }

        if (dropMethod == ItemDropMethod.ON_PLAYER && player != null) {
            world.spawnEntity(ItemEntity(player.world, player.x, player.y, player.z, stack))
        } else if (dropMethod == ItemDropMethod.TO_INVENTORY && player != null) {
            val name = stack.name
            val count = stack.count
            val succeeded = player.giveItemStack(stack)
            if (Cobblemon.config.announceDropItems) {
                player.sendMessage(
                    if (succeeded) lang("drop.item.inventory", count, name.copy().green())
                    else lang("drop.item.full", name).red()
                )
            }
        } else if (dropMethod == ItemDropMethod.ON_ENTITY && entity != null) {
            world.spawnEntity(ItemEntity(entity.world, entity.x, entity.y, entity.z, stack))
        } else {
            world.spawnEntity(ItemEntity(world, pos.x, pos.y, pos.z, stack))
        }
    }

}