package net.skymoe.enchaddons.event.minecraft

import net.minecraft.block.state.IBlockState
import net.minecraft.client.gui.GuiScreen
import net.minecraft.client.renderer.RenderGlobal
import net.minecraft.tileentity.TileEntity
import net.minecraft.world.IBlockAccess
import net.skymoe.enchaddons.event.Event
import net.skymoe.enchaddons.util.asBlockPos
import net.skymoe.enchaddons.util.math.Vec3I

sealed interface RenderEvent : Event {
    sealed interface World : RenderEvent {
        data class Last(
            val context: RenderGlobal,
            val partialTicks: Float,
        ) : World
    }

    sealed interface Render : RenderEvent {
        data object Pre : Render
    }

    sealed interface Entity : RenderEvent {
        data object Post : Entity
    }

    sealed interface Screen : RenderEvent {
        data class Proxy(
            val screen: GuiScreen,
            var mutableScreen: GuiScreen? = null,
        ) : Screen
    }

    sealed interface Block : RenderEvent {
        // You must not modify an air block
        // Be careful when modifying a block to a tile entity or vice versa
        data class ProcessBlockState(
            val blockAccess: IBlockAccess,
            val blockPos: Vec3I,
            val blockState: IBlockState = blockAccess.getBlockState(blockPos.asBlockPos),
            var mutableBlockState: IBlockState = blockState,
        ) : Block

        // You must not modify an air block
        // Be careful when modifying a block to a tile entity or vice versa
        data class ProcessTileEntity(
            val blockAccess: IBlockAccess,
            val blockPos: Vec3I,
            val tileEntity: TileEntity? = blockAccess.getTileEntity(blockPos.asBlockPos),
            var mutableTileEntity: TileEntity? = tileEntity,
        ) : Block
    }
}
