package net.skymoe.enchaddons.impl.feature.awesomemap.commands

import net.minecraft.client.gui.GuiScreen
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.Dungeon
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.DungeonScan
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.ScanUtils
import net.skymoe.enchaddons.util.LogLevel
import net.skymoe.enchaddons.util.MC
import net.skymoe.enchaddons.util.modMessage

class FunnyMapCommands : CommandBase() {
    private val commands = listOf("help", "scan", "roomdata")

    override fun getCommandName(): String = "funnymap"

    override fun getCommandAliases(): List<String> = listOf("fmap", "fm")

    override fun getCommandUsage(sender: ICommandSender): String = "/$commandName"

    override fun getRequiredPermissionLevel(): Int = 0

    override fun processCommand(
        sender: ICommandSender,
        args: Array<String>,
    ) {
        if (args.isEmpty()) {
//            display = Config.gui()
            return
        }
        when (args[0]) {
            // Help command
            "help" -> {
//                UChat.chat(
//                    """
//                        #§b§l<§fFunnyMap Commands§b§l>
//                        #  §b/funnymap §9> §3Opens the main mod GUI. §7(Alias: fm, fmap)
//                        #  §b/§ffunnymap §bscan §9> §3Rescans the map.
//                        #  §b/§ffunnymap §broomdata §9> §3Copies current room data or room core to clipboard.
//                    """.trimMargin("#"),
//                )
            }
            // Scans the dungeon
            "scan" -> {
                Dungeon.reset()
                DungeonScan.scan()
            }
            // Copies room data or room core to clipboard
            "roomdata" -> {
                val pos = ScanUtils.getRoomCentre(MC.thePlayer.posX.toInt(), MC.thePlayer.posZ.toInt())
                val data = ScanUtils.getRoomData(pos.first, pos.second)
                if (data != null) {
                    GuiScreen.setClipboardString(data.toString())
                    modMessage("§aCopied room data to clipboard.", LogLevel.INFO)
                } else {
                    GuiScreen.setClipboardString(ScanUtils.getCore(pos.first, pos.second).toString())
                    modMessage("§cExisting room data not found. §aCopied room core to clipboard.", LogLevel.WARN)
                }
            }
            // Unknown command help message
            else -> modMessage("§cUnknown command. Use §b/§ffunnymap help §cfor a list of commands.", LogLevel.WARN)
        }
    }

    override fun addTabCompletionOptions(
        sender: ICommandSender,
        args: Array<String>,
        pos: BlockPos,
    ): MutableList<String> {
        if (args.size == 1) {
            return getListOfStringsMatchingLastWord(args, commands)
        }
        return mutableListOf()
    }
}
