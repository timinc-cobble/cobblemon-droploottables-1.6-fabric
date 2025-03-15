package us.timinc.mc.cobblemon.droploottables.extensions

import com.cobblemon.mod.common.api.Priority
import com.cobblemon.mod.common.pokemon.Pokemon

fun Pokemon.hasHiddenAbility(): Boolean =
    form.abilities.filter { it.priority == Priority.LOW }.map { it.template.name }.contains(ability.name)