package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.AxisAlignedBB
import net.skymoe.enchaddons.event.minecraft.RenderEvent
import net.skymoe.enchaddons.impl.config.EnchAddonsConfig
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomState
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location.inBoss
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.getInterpolatedPosition
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils3D
import net.skymoe.enchaddons.util.MC

object WitherDoorESP {
    fun onRender(event: RenderEvent.World.Last) {
        if (inBoss || EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorESP == 0) return

        val (x, y, z) = MC.renderViewEntity.getInterpolatedPosition(event.partialTicks)
        GlStateManager.translate(-x, -y, -z)
        Dungeon.espDoors.forEach { door ->
            if (EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorESP == 1 && door.state == RoomState.UNDISCOVERED) return@forEach
            val aabb = AxisAlignedBB(door.x - 1.0, 69.0, door.z - 1.0, door.x + 2.0, 73.0, door.z + 2.0)
            RenderUtils3D.drawBox(
                aabb,
                if (Dungeon.Info.keys > 0) {
                    EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorKeyColor
                        .toJavaColor()
                } else {
                    EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorNoKeyColor
                        .toJavaColor()
                },
                EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorOutlineWidth,
                EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorOutline,
                EnchAddonsConfig.dungeonConfig.awesomeMapConfig.witherDoorFill,
                true,
            )
        }
        GlStateManager.translate(x, y, z)
    }
}
