package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import com.google.gson.Gson
import com.google.gson.JsonIOException
import com.google.gson.JsonSyntaxException
import com.google.gson.reflect.TypeToken
import net.minecraft.block.Block
import net.minecraft.util.BlockPos
import net.skymoe.enchaddons.impl.MOD_ID
import net.skymoe.enchaddons.impl.feature.awesomemap.core.RoomData
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.Room
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Utils.equalsOneOf
import net.skymoe.enchaddons.util.MC
import kotlin.math.roundToInt

object ScanUtils {
    val roomList: Set<RoomData> =
        try {
            Gson().fromJson(
                javaClass.getResource("/assets/$MOD_ID/awesomemap/rooms.json")!!.readBytes().toString(Charsets.UTF_8),
                object : TypeToken<Set<RoomData>>() {}.type,
            )
        } catch (e: JsonSyntaxException) {
            println("Error parsing FunnyMap room data.")
            setOf()
        } catch (e: JsonIOException) {
            println("Error reading FunnyMap room data.")
            setOf()
        }

    fun getRoomData(
        x: Int,
        z: Int,
    ): RoomData? = getRoomData(getCore(x, z))

    fun getRoomData(hash: Int): RoomData? = roomList.find { hash in it.cores }

    fun getRoomCentre(
        posX: Int,
        posZ: Int,
    ): Pair<Int, Int> {
        val roomX = ((posX - DungeonScan.START_X) / 32f).roundToInt()
        val roomZ = ((posZ - DungeonScan.START_Z) / 32f).roundToInt()
        return Pair(roomX * 32 + DungeonScan.START_X, roomZ * 32 + DungeonScan.START_Z)
    }

    fun getRoomFromPos(pos: BlockPos): Room? {
        val x = ((pos.x - DungeonScan.START_X + 15) shr 5)
        val z = ((pos.z - DungeonScan.START_Z + 15) shr 5)
        val room = Dungeon.Info.dungeonList.getOrNull(x * 2 + z * 22)
        return if (room is Room) room else null
    }

    fun getCore(
        x: Int,
        z: Int,
    ): Int {
        val sb = StringBuilder(150)
        val chunk = MC.theWorld.getChunkFromChunkCoords(x shr 4, z shr 4)
        val height = chunk.getHeightValue(x and 15, z and 15).coerceIn(11..140)
        sb.append(CharArray(140 - height) { '0' })
        var bedrock = 0
        for (y in height downTo 12) {
            val id = Block.getIdFromBlock(chunk.getBlock(BlockPos(x, y, z)))
            if (id == 0 && bedrock >= 2 && y < 69) {
                sb.append(CharArray(y - 11) { '0' })
                break
            }

            if (id == 7) {
                bedrock++
            } else {
                bedrock = 0
                if (id.equalsOneOf(5, 54, 146)) continue
            }

            sb.append(id)
        }
        return sb.toString().hashCode()
    }
}
