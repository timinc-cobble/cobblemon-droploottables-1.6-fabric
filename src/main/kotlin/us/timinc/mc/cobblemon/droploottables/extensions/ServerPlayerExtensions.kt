package us.timinc.mc.cobblemon.droploottables.extensions

import com.cobblemon.mod.common.Cobblemon
import com.cobblemon.mod.common.api.pokedex.PokedexManager
import net.minecraft.server.level.ServerPlayer

fun ServerPlayer.getPokedexManager(): PokedexManager = Cobblemon.playerDataManager.getPokedexData(this)