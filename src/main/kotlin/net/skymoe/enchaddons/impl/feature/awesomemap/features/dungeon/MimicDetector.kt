package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.block.state.IBlockState
import net.minecraft.entity.Entity
import net.minecraft.entity.monster.EntityZombie
import net.minecraft.init.Blocks
import net.minecraft.tileentity.TileEntityChest
import net.minecraft.util.BlockPos
import net.skymoe.enchaddons.api.setDefault
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.feature.config.invoke
import net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon.ScanUtils.getRoomFromPos
import net.skymoe.enchaddons.util.MC

object MimicDetector {
    var mimicOpenTime = 0L
    var mimicPos: BlockPos? = null

    fun onBlockChange(
        pos: BlockPos,
        old: IBlockState,
        new: IBlockState,
    ) {
        if (old.block == Blocks.trapped_chest && new.block == Blocks.air) {
            mimicOpenTime = System.currentTimeMillis()
            mimicPos = pos
        }
    }

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
        AwesomeMap.config.notification.onMimicKilled.invoke(
            logger = AwesomeMap.logger,
            placeholder = {
                setDefault()
            },
        )
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
