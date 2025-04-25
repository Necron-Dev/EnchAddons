package net.skymoe.enchaddons.impl.config.feature

import cc.polyfrost.oneconfig.config.annotations.*
import cc.polyfrost.oneconfig.config.annotations.Number
import cc.polyfrost.oneconfig.config.core.OneColor
import net.skymoe.enchaddons.feature.awesomemap.AWESOME_MAP_INFO
import net.skymoe.enchaddons.feature.awesomemap.AwesomeMapConfig
import net.skymoe.enchaddons.feature.awesomemap.Notification
import net.skymoe.enchaddons.impl.config.AdvancedHUD
import net.skymoe.enchaddons.impl.config.ConfigImpl
import net.skymoe.enchaddons.impl.config.adapter.Extract
import net.skymoe.enchaddons.impl.config.gui.GUIBackground
import net.skymoe.enchaddons.impl.config.impl.NotificationOptionImpl

class AwesomeMapConfigImpl :
    ConfigImpl(AWESOME_MAP_INFO),
    AwesomeMapConfig {
    @Switch(
        name = "Auto Scan",
        description = "Automatically scans when entering dungeon. Manual scan can be done with /fmap scan.",
        category = "General",
        subcategory = "Scanning",
    )
    override var autoScan = true

    @Switch(
        name = "Chat Info",
        description = "Show dungeon overview information after scanning.",
        category = "General",
        subcategory = "Scanning",
    )
    override var scanChatInfo = true

    @Switch(
        name = "Enabled",
        description = "Hides unopened rooms. Still uses scanning to identify all rooms.",
        category = "General",
        subcategory = "Legit Mode",
    )
    override var legitMode = false

    @HUD(
        name = "Map",
        category = "Map",
    )
    var hud = AdvancedHUD()

    @Extract(
        category = "Map",
    )
    var background = GUIBackground()

    @Number(
        name = "Room Radius",
        category = "Map",
        subcategory = "Room Render",
        min = 0F,
        max = 10F,
    )
    var mapRoomRadius = 4.0f

    @Number(
        name = "L-Shape Room Inner Radius",
        category = "Map",
        subcategory = "Room Render",
        min = 0F,
        max = 10F,
    )
    var mapLShapeRoomInnerRadius = 2.0f

    @Switch(
        name = "Rotate Map",
        description = "Rotates map to follow the player.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapRotate = false

    @Switch(
        name = "Center Map",
        description = "Centers the map on the player if Rotate Map is enabled.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapCenter = false

    @Switch(
        name = "Dynamic Rotate",
        description = "Keeps the entrance room at the bottom. Does not work with rotate map.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapDynamicRotate = false

    @Switch(
        name = "Hide In Boss",
        description = "Hides the map in boss.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapHideInBoss = false

    @Dropdown(
        name = "Show Player Names",
        description = "Show player name under player head",
        category = "Map",
        subcategory = "Tweaks",
        options = ["Off", "Holding Leap", "Always"],
    )
    override var playerHeads = 0

    @Switch(
        name = "Vanilla Head Marker",
        description = "Uses the vanilla head marker for yourself.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapVanillaMarker = false

    @Slider(
        name = "Map Text Scale",
        description = "Scale of room names and secret counts relative to map size.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 2F,
    )
    override var textScale = 0.75f

    @Slider(
        name = "Player Heads Scale",
        description = "Scale of player heads relative to map size.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 2F,
    )
    override var playerHeadScale = 1f

    @Slider(
        name = "Player Heads Radius",
        description = "Radius of player heads.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 10F,
    )
    var playerHeadRadius = 2.0f

    @Slider(
        name = "Player Name Scale",
        description = "Scale of player names relative to head size.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 2F,
    )
    override var playerNameScale = .8f

    @Switch(
        name = "Dark Undiscovered Rooms",
        description = "Darkens unentered rooms.",
        category = "Rooms",
        subcategory = "Render",
    )
    override var mapDarkenUndiscovered = true

    @Slider(
        name = "Darken Multiplier",
        description = "How much to darken undiscovered rooms.",
        category = "Rooms",
        subcategory = "Render",
        min = 0F,
        max = 1F,
    )
    override var mapDarkenPercent = 0.4f

    @Switch(
        name = "Gray Undiscovered Rooms",
        description = "Grayscale unentered rooms.",
        category = "Rooms",
        subcategory = "Render",
    )
    override var mapGrayUndiscovered = false

    @Dropdown(
        name = "Room Names",
        description = "Shows names of rooms on map.",
        category = "Rooms",
        subcategory = "Text",
        options = ["None", "Puzzles / Trap", "All"],
    )
    override var mapRoomNames = 2

    @Dropdown(
        name = "Room Secrets",
        description = "Shows total secrets of rooms on map.",
        category = "Rooms",
        subcategory = "Text",
        options = ["Off", "On", "Replace Checkmark"],
    )
    override var mapRoomSecrets = 0

    @Switch(
        name = "Center Room Names",
        description = "Center room names.",
        subcategory = "Text",
        category = "Rooms",
    )
    override var mapCenterRoomName = true

    @Switch(
        name = "Color Text",
        description = "Colors name and secret count based on room state.",
        subcategory = "Text",
        category = "Rooms",
    )
    override var mapColorText = true

    @Dropdown(
        name = "Room Checkmarks",
        description = "Adds room checkmarks based on room state.",
        category = "Rooms",
        subcategory = "Checkmarks",
        options = ["None", "Default", "NEU", "Legacy"],
    )
    override var mapCheckmark = 1

    @Switch(
        name = "Center Room Checkmarks",
        description = "Center room checkmarks.",
        subcategory = "Checkmarks",
        category = "Rooms",
    )
    override var mapCenterCheckmark = true

    @Color(
        name = "Blood Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorBloodDoor = OneColor(231, 0, 0)

    @Color(
        name = "Entrance Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorEntranceDoor = OneColor(20, 133, 0)

    @Color(
        name = "Normal Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorRoomDoor = OneColor(92, 52, 14)

    @Color(
        name = "Wither Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorWitherDoor = OneColor(0, 0, 0)

    @Color(
        name = "Opened Wither Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorOpenWitherDoor = OneColor(92, 52, 14)

    @Color(
        name = "Unopened Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    override var colorUnopenedDoor = OneColor(65, 65, 65)

    @Color(
        name = "Blood Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorBlood = OneColor(255, 0, 0)

    @Color(
        name = "Entrance Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorEntrance = OneColor(20, 133, 0)

    @Color(
        name = "Fairy Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorFairy = OneColor(224, 0, 255)

    @Color(
        name = "Miniboss Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorMiniboss = OneColor(254, 223, 0)

    @Color(
        name = "Normal Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorRoom = OneColor(107, 58, 17)

    @Color(
        name = "Mimic Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorRoomMimic = OneColor(186, 66, 52)

    @Color(
        name = "Puzzle Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorPuzzle = OneColor(117, 0, 133)

    @Color(
        name = "Rare Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorRare = OneColor(255, 203, 89)

    @Color(
        name = "Trap Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorTrap = OneColor(216, 127, 51)

    @Color(
        name = "Unopened Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    override var colorUnopened = OneColor(65, 65, 65)

    @Color(
        name = "Cleared Room Text",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    override var colorTextCleared = OneColor(255, 255, 255)

    @Color(
        name = "Uncleared Room Text",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    override var colorTextUncleared = OneColor(170, 170, 170)

    @Color(
        name = "Green Room Text",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    override var colorTextGreen = OneColor(85, 255, 85)

    @Color(
        name = "Failed Room Text",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    override var colorTextFailed = OneColor(255, 255, 255)

    @Switch(
        name = "Show Score",
        description = "Shows separate score element.",
        category = "Score",
        subcategory = "Toggle",
    )
    override var scoreElementEnabled = false

    @Switch(
        name = "Assume Spirit",
        description = "Assume everyone has a legendary spirit pet.",
        category = "Score",
        subcategory = "Toggle",
    )
    override var scoreAssumeSpirit = true

    @Switch(
        name = "Minimized Text",
        description = "Shortens description for score elements.",
        category = "Score",
        subcategory = "Toggle",
    )
    override var scoreMinimizedName = false

    @Switch(
        name = "Hide in Boss",
        category = "Score",
        subcategory = "Toggle",
    )
    override var scoreHideInBoss = false

    @Number(
        name = "Score Calc X",
        category = "Score",
        subcategory = "Size",
        min = 0F,
        max = 100F,
    )
    override var scoreX = 10

    @Number(
        name = "Score Calc Y",
        category = "Score",
        subcategory = "Size",
        min = 0F,
        max = 100F,
    )
    override var scoreY = 10

    @Slider(
        name = "Score Calc Size",
        category = "Score",
        subcategory = "Size",
        min = 0.1F,
        max = 4F,
    )
    override var scoreScale = 1f

    @Dropdown(
        name = "Score",
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "On", "Separate"],
    )
    override var scoreTotalScore = 2

    @Dropdown(
        name = "Secrets",
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "Total", "Total and Missing"],
    )
    override var scoreSecrets = 1

    @Switch(
        name = "Crypts",
        category = "Score",
        subcategory = "Elements",
    )
    override var scoreCrypts = false

    @Switch(
        name = "Mimic",
        category = "Score",
        subcategory = "Elements",
    )
    override var scoreMimic = false

    @Switch(
        name = "Deaths",
        category = "Score",
        subcategory = "Elements",
    )
    override var scoreDeaths = false

    @Dropdown(
        name = "Puzzles",
        category = "Score",
        subcategory = "Elements",
        options = ["Off", "Total", "Completed and Total"],
    )
    override var scorePuzzles = 0

    @Switch(
        name = "Show Run Information",
        description = "Shows run information under map.",
        category = "Run Information",
        subcategory = "Toggle",
    )
    override var mapShowRunInformation = true

    @Switch(
        name = "Score",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationScore = true

    @Dropdown(
        name = "Secrets",
        category = "Run Information",
        subcategory = "Elements",
        options = ["Off", "Total", "Total and Missing"],
    )
    override var runInformationSecrets = 1

    @Switch(
        name = "Crypts",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationCrypts = true

    @Switch(
        name = "Mimic",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationMimic = true

    @Switch(
        name = "Deaths",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationDeaths = true

    @Text(
        name = "Hypixel API Key",
        category = "Other Features",
        secure = true,
    )
    override var apiKey = ""

    @Switch(
        name = "Show Team Info",
        description = "Shows team member secrets and room times at end of run. Requires a valid API key.",
        category = "Other Features",
    )
    override var teamInfo = false

    @Switch(
        name = "Mimic Message",
        description = "Sends party message when a mimic is killed. Detects most instant kills.",
        category = "Other Features",
        subcategory = "Mimic Message",
    )
    override var mimicMessageEnabled = false

    @Text(
        name = "Mimic Message Text",
        category = "Other Features",
        subcategory = "Mimic Message",
    )
    override var mimicMessage = "Mimic Killed!"

    @Dropdown(
        name = "Wither Door ESP",
        description = "Boxes unopened wither doors.",
        category = "Other Features",
        subcategory = "Wither Door",
        options = ["Off", "First", "All"],
    )
    override var witherDoorESP = 0

    @Color(
        name = "No Key Color",
        category = "Other Features",
        subcategory = "Wither Door",
        allowAlpha = true,
    )
    override var witherDoorNoKeyColor = OneColor(255, 0, 0)

    @Color(
        name = "Has Key Color",
        category = "Other Features",
        subcategory = "Wither Door",
        allowAlpha = true,
    )
    override var witherDoorKeyColor = OneColor(0, 255, 0)

    @Slider(
        name = "Door Outline Width",
        category = "Other Features",
        subcategory = "Wither Door",
        min = 1f,
        max = 10f,
    )
    override var witherDoorOutlineWidth = 3f

    @Slider(
        name = "Door Outline Opacity",
        category = "Other Features",
        subcategory = "Wither Door",
        min = 0F,
        max = 10F,
    )
    override var witherDoorOutline = 1f

    @Slider(
        name = "Door Fill Opacity",
        category = "Other Features",
        subcategory = "Wither Door",
        min = 0F,
        max = 10F,
    )
    override var witherDoorFill = 0.25f

    @Switch(
        name = "Force Skyblock",
        description = "Disables in skyblock and dungeon checks. Don't enable unless you know what you're doing.",
        category = "Debug",
    )
    override var forceSkyblock = false

    @Switch(
        name = "Paul Score",
        description = "Assumes paul perk is active to give 10 bonus score.",
        category = "Debug",
    )
    override var paulBonus = false

    @Switch(
        name = "Beta Rendering",
        category = "Debug",
    )
    override var renderBeta = false

    @Text(
        name = "Custom Prefix",
        category = "Other Features",
    )
    override var customPrefix = ""

    class NotificationImpl : Notification {
        @Transient
        @Header(
            text = "Notification On Score 270",
            size = 2,
            category = "Notification",
        )
        val headerOnScore270 = false

        @Extract(
            category = "Notification",
        )
        override var onScore270: NotificationOptionImpl = NotificationOptionImpl()

        @Transient
        @Header(
            text = "Notification On Score 300",
            size = 2,
            category = "Notification",
        )
        val headerOnScore300 = false

        @Extract(
            category = "Notification",
        )
        override var onScore300: NotificationOptionImpl = NotificationOptionImpl()

        @Transient
        @Header(
            text = "Notification On Mimic Killed",
            size = 2,
            category = "Notification",
        )
        val headerOnMimicKilled = false

        @Extract(
            category = "Notification",
        )
        override var onMimicKilled: NotificationOptionImpl = NotificationOptionImpl()
    }

    @Extract(
        category = "Notification",
    )
    override var notification = NotificationImpl()
}
