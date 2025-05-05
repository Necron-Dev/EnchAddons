package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.BlockPos
import net.skymoe.enchaddons.EA
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMapEvent
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.ScanUtils.getRoomFromPos
import net.skymoe.enchaddons.util.MC

object MimicDetector {
    var mimicOpenTime = 0L
    var mimicPos: BlockPos? = null

    fun checkMimicDead() {
        if (RunInformation.mimicKilled) return
        if (mimicOpenTime == 0L) return
        if (System.currentTimeMillis() - mimicOpenTime < 750) return
        if (MC.thePlayer.getDistanceSq(mimicPos) < 400) {
            if (MC.theWorld.loadedEntityList.none {
                    it is EntityZombie &&
                        it.isChild &&
                        it
                            .getCurrentArmor(3)
                            ?.getSubCompound("SkullOwner", false)
                            ?.getString("Id") == "bcb486a4-0cb5-35db-93f0-039fbdde03f0"
                }
            ) {
                setMimicKilled()
            }
        }
    }

    fun setMimicKilled() {
        RunInformation.mimicKilled = true
        AwesomeMapEvent
            .MimicKilled(RunInformation.timeElapsed)
            .also(EA.eventDispatcher)
    }

    fun isMimic(entity: Entity): Boolean {
        if (entity is EntityZombie && entity.isChild) {
            for (i in 0..3) {
                if (entity.getCurrentArmor(i) != null) return false
            }
            return true
        }
        return false
    }

    fun findMimic(): String? {
        MC.theWorld.loadedTileEntityList
            .filter { it is TileEntityChest && it.chestType == 1 }
            .groupingBy { getRoomFromPos(it.pos)?.data?.name }
            .eachCount()
            .forEach { (room, trappedChests) ->
                Dungeon.Info.uniqueRooms
                    .find { it.name == room && it.mainRoom.data.trappedChests < trappedChests }
                    ?.let {
                        it.hasMimic = true
                        MapRenderList.renderUpdated = true
                        return it.name
                    }
            }
        return null
    }
}
