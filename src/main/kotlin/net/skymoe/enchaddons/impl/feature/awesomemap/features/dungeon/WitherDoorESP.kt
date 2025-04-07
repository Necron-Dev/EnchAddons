package net.skymoe.enchaddons.impl.feature.awesomemap.features.dungeon

import net.minecraft.client.renderer.GlStateManager
import net.minecraft.util.AxisAlignedBB
import net.skymoe.enchaddons.event.minecraft.RenderEvent
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMap
import net.skymoe.enchaddons.impl.feature.awesomemap.core.map.RoomState
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.Location.inBoss
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils
import net.skymoe.enchaddons.impl.feature.awesomemap.utils.RenderUtils.getInterpolatedPosition
import net.skymoe.enchaddons.util.MC

object WitherDoorESP {
    fun onRender(event: RenderEvent.World.Last) {
        if (inBoss || AwesomeMap.config.witherDoorESP == 0) return

        val (x, y, z) = MC.renderViewEntity.getInterpolatedPosition(event.partialTicks)
        GlStateManager.translate(-x, -y, -z)
        Dungeon.espDoors.forEach { door ->
            if (AwesomeMap.config.witherDoorESP == 1 && door.state == RoomState.UNDISCOVERED) return@forEach
            val aabb = AxisAlignedBB(door.x - 1.0, 69.0, door.z - 1.0, door.x + 2.0, 73.0, door.z + 2.0)
            RenderUtils.drawBox(
                aabb,
                if (Dungeon.Info.keys > 0) {
                    AwesomeMap.config.witherDoorKeyColor.toJavaColor()
                } else {
                    AwesomeMap.config.witherDoorNoKeyColor.toJavaColor()
                },
                AwesomeMap.config.witherDoorOutlineWidth,
                AwesomeMap.config.witherDoorOutline,
                AwesomeMap.config.witherDoorFill,
                true,
            )
        }
        GlStateManager.translate(x, y, z)
    }
}
