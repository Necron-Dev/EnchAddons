package net.skymoe.enchaddons.impl.feature.awesomemap.utils

import net.minecraft.item.ItemStack
import net.minecraft.util.StringUtils
import net.skymoe.enchaddons.util.MC

object Utils {
    fun Any?.equalsOneOf(vararg other: Any): Boolean = other.any { this == it }

    fun runMinecraftThread(run: () -> Unit) {
        if (!MC.isCallingFromMinecraftThread) {
            MC.addScheduledTask(run)
        } else {
            run()
        }
    }

    fun String.removeFormatting(): String = StringUtils.stripControlCodes(this)

    val ItemStack.itemID: String
        get() = this.getSubCompound("ExtraAttributes", false)?.getString("id") ?: ""
}
