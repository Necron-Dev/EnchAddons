package net.skymoe.enchaddons.util

import net.minecraft.util.IChatComponent

enum class LogLevel(
    val component: IChatComponent,
) {
    INFO("§bEA§r §7»§r ".asComponent()),
    WARN("§6EA§r §7»§r ".asComponent()),
    ERROR("§cEA§r §7»§r ".asComponent()),
}

fun modMessage(
    message: String,
    logLevel: LogLevel,
) {
    modMessage(message.asComponent(), logLevel)
}

fun modMessage(
    message: IChatComponent,
    logLevel: LogLevel,
) {
    printChat(logLevel.component.createCopy().appendSibling(message))
}
