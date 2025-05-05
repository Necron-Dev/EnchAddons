package net.skymoe.enchaddons.util

import net.minecraft.event.ClickEvent
import net.minecraft.event.HoverEvent
import net.minecraft.util.EnumChatFormatting
import net.minecraft.util.IChatComponent

class ComponentBuilderScope(
    private val parentStyle: Style,
) {
    val elements: MutableList<Element> = mutableListOf()

    data class Style(
        val color: TextColor = TextColor.WHITE,
        val bold: Boolean = false,
        val italic: Boolean = false,
        val underlined: Boolean = false,
        val strikethrough: Boolean = false,
        val obfuscated: Boolean = false,
        val clickEvent: ClickEvent? = null,
        val hoverEvent: HoverEvent? = null,
    )

    data class Element(
        val text: String,
        var style: Style,
        val component: IChatComponent? = null,
    ) {
        private fun modifyStyle(modifier: Style.() -> Style) = apply { style = modifier(style) }

        val black get() = modifyStyle { copy(color = TextColor.BLACK) }
        val darkBlue get() = modifyStyle { copy(color = TextColor.DARK_BLUE) }
        val darkGreen get() = modifyStyle { copy(color = TextColor.DARK_GREEN) }
        val darkAqua get() = modifyStyle { copy(color = TextColor.DARK_AQUA) }
        val darkRed get() = modifyStyle { copy(color = TextColor.DARK_RED) }
        val darkPurple get() = modifyStyle { copy(color = TextColor.DARK_PURPLE) }
        val gold get() = modifyStyle { copy(color = TextColor.GOLD) }
        val gray get() = modifyStyle { copy(color = TextColor.GRAY) }
        val darkGray get() = modifyStyle { copy(color = TextColor.DARK_GRAY) }
        val blue get() = modifyStyle { copy(color = TextColor.BLUE) }
        val green get() = modifyStyle { copy(color = TextColor.GREEN) }
        val aqua get() = modifyStyle { copy(color = TextColor.AQUA) }
        val red get() = modifyStyle { copy(color = TextColor.RED) }
        val lightPurple get() = modifyStyle { copy(color = TextColor.LIGHT_PURPLE) }
        val yellow get() = modifyStyle { copy(color = TextColor.YELLOW) }
        val white get() = modifyStyle { copy(color = TextColor.WHITE) }

        val bold get() = modifyStyle { copy(bold = true) }
        val italic get() = modifyStyle { copy(italic = true) }
        val underlined get() = modifyStyle { copy(underlined = true) }
        val strikethrough get() = modifyStyle { copy(strikethrough = true) }
        val obfuscated get() = modifyStyle { copy(obfuscated = true) }
    }

    private fun String.toElement() = Element(this, parentStyle).also(elements::add)

    val String.append get() = toElement()

    val IChatComponent.append get() = elements.add(Element("", parentStyle, this))

    val String.black get() = toElement().black
    val String.darkBlue get() = toElement().darkBlue
    val String.darkGreen get() = toElement().darkGreen
    val String.darkAqua get() = toElement().darkAqua
    val String.darkRed get() = toElement().darkRed
    val String.darkPurple get() = toElement().darkPurple
    val String.gold get() = toElement().gold
    val String.gray get() = toElement().gray
    val String.darkGray get() = toElement().darkGray
    val String.blue get() = toElement().blue
    val String.green get() = toElement().green
    val String.aqua get() = toElement().aqua
    val String.red get() = toElement().red
    val String.lightPurple get() = toElement().lightPurple
    val String.yellow get() = toElement().yellow
    val String.white get() = toElement().white

    val String.bold get() = toElement().bold
    val String.italic get() = toElement().italic
    val String.underlined get() = toElement().underlined
    val String.strikethrough get() = toElement().strikethrough
    val String.obfuscated get() = toElement().obfuscated

    fun build(): IChatComponent =
        "".asComponent().apply {
            elements.forEach { element ->
                element.component?.let {
                    siblings.add(it)
                    return@forEach
                }

                siblings.add(
                    element.text.asComponent().apply {
                        chatStyle.color = element.style.color.formatting
                        chatStyle.bold = element.style.bold
                        chatStyle.italic = element.style.italic
                        chatStyle.underlined = element.style.underlined
                        chatStyle.strikethrough = element.style.strikethrough
                        chatStyle.obfuscated = element.style.obfuscated
                        chatStyle.chatClickEvent = element.style.clickEvent
                        chatStyle.chatHoverEvent = element.style.hoverEvent
                    },
                )
            }
        }
}

fun buildComponent(scope: ComponentBuilderScope.() -> Unit): IChatComponent =
    ComponentBuilderScope(ComponentBuilderScope.Style())
        .apply(scope)
        .build()

enum class TextColor(
    val formatting: EnumChatFormatting,
) {
    BLACK(EnumChatFormatting.BLACK),
    DARK_BLUE(EnumChatFormatting.DARK_BLUE),
    DARK_GREEN(EnumChatFormatting.DARK_GREEN),
    DARK_AQUA(EnumChatFormatting.DARK_AQUA),
    DARK_RED(EnumChatFormatting.DARK_RED),
    DARK_PURPLE(EnumChatFormatting.DARK_PURPLE),
    GOLD(EnumChatFormatting.GOLD),
    GRAY(EnumChatFormatting.GRAY),
    DARK_GRAY(EnumChatFormatting.DARK_GRAY),
    BLUE(EnumChatFormatting.BLUE),
    GREEN(EnumChatFormatting.GREEN),
    AQUA(EnumChatFormatting.AQUA),
    RED(EnumChatFormatting.RED),
    LIGHT_PURPLE(EnumChatFormatting.LIGHT_PURPLE),
    YELLOW(EnumChatFormatting.YELLOW),
    WHITE(EnumChatFormatting.WHITE),
}
