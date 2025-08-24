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
import net.skymoe.enchaddons.impl.config.announcement.DisclaimerAtOwnRisk
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

    @Number(
        name = "Room Connector Width",
        category = "Map",
        subcategory = "Room Render",
        min = 0F,
        max = 16F,
    )
    var mapRoomConnector = 6.0f

    @Dropdown(
        name = "Rotate Mode",
        description = "Rotates map to specific mode.",
        category = "Map",
        subcategory = "Tweaks",
        options = ["Off", "Player Direction", "Entrance Room At Bottom"],
    )
    override var mapRotateMode = 2

    @Switch(
        name = "Center Map",
        description = "Centers the map on the player if Rotate Map is enabled.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapCenter = false

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
        name = "Self Vanilla Head Marker",
        description = "Uses the vanilla head marker for yourself.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapVanillaMarker = false

    @Switch(
        name = "Teammates Vanilla Head Marker",
        description = "Uses the vanilla head marker for teammates.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapVanillaMarkerTeammates = false

    @Switch(
        name = "Head Color Border",
        description = "Add color border to player heads",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapPlayerHeadColorBorder = false

    @Switch(
        name = "Clip Map",
        description = "Clip map which out of the HUD region.",
        category = "Map",
        subcategory = "Tweaks",
    )
    override var mapClip = true

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
        name = "Vanilla Marker Scale",
        description = "Scale of vanilla marker relative to map size.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 2F,
    )
    override var vanillaMarkerScale = 1f

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
        name = "Class Color Border Size",
        description = "Scale of player heads relative to map size.",
        category = "Map",
        subcategory = "Size",
        min = 0F,
        max = 2F,
    )
    var colorBorderSize = 1f

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

    @Switch(
        name = "Room Checkmark",
        description = "Adds room checkmarks based on room state.",
        category = "Rooms",
        subcategory = "Checkmarks",
    )
    override var mapCheckmark = true

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
    var colorBloodDoor = OneColor(231, 0, 0)

    @Color(
        name = "Entrance Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    var colorEntranceDoor = OneColor(20, 133, 0)

    @Color(
        name = "Normal Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    var colorRoomDoor = OneColor(92, 52, 14)

    @Color(
        name = "Wither Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    var colorWitherDoor = OneColor(0, 0, 0)

    @Color(
        name = "Opened Wither Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    var colorOpenWitherDoor = OneColor(92, 52, 14)

    @Color(
        name = "Unopened Door",
        category = "Colors",
        subcategory = "Doors",
        allowAlpha = true,
    )
    var colorUnopenedDoor = OneColor(65, 65, 65)

    @Color(
        name = "Blood Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorBlood = OneColor(255, 0, 0)

    @Color(
        name = "Entrance Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorEntrance = OneColor(20, 133, 0)

    @Color(
        name = "Fairy Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorFairy = OneColor(224, 0, 255)

    @Color(
        name = "Miniboss Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorMiniboss = OneColor(254, 223, 0)

    @Color(
        name = "Normal Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorRoom = OneColor(107, 58, 17)

    @Color(
        name = "Mimic Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorRoomMimic = OneColor(186, 66, 52)

    @Color(
        name = "Puzzle Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorPuzzle = OneColor(117, 0, 133)

    @Color(
        name = "Rare Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorRare = OneColor(255, 203, 89)

    @Color(
        name = "Trap Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorTrap = OneColor(216, 127, 51)

    @Color(
        name = "Unopened Room",
        category = "Colors",
        subcategory = "Rooms",
        allowAlpha = true,
    )
    var colorUnopened = OneColor(65, 65, 65)

    @Color(
        name = "Cleared Room",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    var colorTextCleared = OneColor(255, 255, 255)

    @Color(
        name = "Uncleared Room",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    var colorTextUncleared = OneColor(170, 170, 170)

    @Color(
        name = "Green Room",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    var colorTextGreen = OneColor(85, 255, 85)

    @Color(
        name = "Failed Room",
        category = "Colors",
        subcategory = "Text",
        allowAlpha = true,
    )
    var colorTextFailed = OneColor(255, 255, 255)

    @Color(
        name = "Cleared Room",
        category = "Colors",
        subcategory = "CheckMark",
        allowAlpha = true,
    )
    var colorCheckMarkCleared = OneColor(255, 255, 255)

    @Color(
        name = "Green Room",
        category = "Colors",
        subcategory = "CheckMark",
        allowAlpha = true,
    )
    var colorCheckMarkGreen = OneColor(85, 255, 85)

    @Color(
        name = "Failed Room",
        category = "Colors",
        subcategory = "CheckMark",
        allowAlpha = true,
    )
    var colorCheckMarkFailed = OneColor(255, 255, 255)

    @Color(
        name = "Archer",
        category = "Colors",
        subcategory = "Class",
        allowAlpha = true,
    )
    var colorTextArcher = OneColor(255, 170, 0)

    @Color(
        name = "Tank",
        category = "Colors",
        subcategory = "Class",
        allowAlpha = true,
    )
    var colorTextTank = OneColor(85, 255, 85)

    @Color(
        name = "Berserk",
        category = "Colors",
        subcategory = "Class",
        allowAlpha = true,
    )
    var colorTextBerserk = OneColor(255, 85, 85)

    @Color(
        name = "Mage",
        category = "Colors",
        subcategory = "Class",
        allowAlpha = true,
    )
    var colorTextMage = OneColor(85, 255, 255)

    @Color(
        name = "Healer",
        category = "Colors",
        subcategory = "Class",
        allowAlpha = true,
    )
    var colorTextHealer = OneColor(255, 85, 255)

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
        name = "Prince",
        category = "Score",
        subcategory = "Elements",
    )
    override var scorePrince = false

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
        name = "Prince",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationPrince = true

    @Switch(
        name = "Deaths",
        category = "Run Information",
        subcategory = "Elements",
    )
    override var runInformationDeaths = true

    @Text(
        name = "Score",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textScore = "Score"

    @Text(
        name = "Secrets",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textSecrets = "Secrets"

    @Text(
        name = "Crypts",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textCrypts = "Crypts"

    @Text(
        name = "Mimic",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textMimic = "Mimic"

    @Text(
        name = "Prince",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textPrince = "Prince"

    @Text(
        name = "Mimic Yes",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textMimicYes = "✔"

    @Text(
        name = "Mimic No",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textMimicNo = "✘"

    @Text(
        name = "Prince Yes",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textPrinceYes = "✔"

    @Text(
        name = "Prince No",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textPrinceNo = "✔"

    @Text(
        name = "Death",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textDeaths = "Deaths"

    @Text(
        name = "Puzzles",
        category = "Placeholder",
        subcategory = "Full",
    )
    var textPuzzles = "Puzzles"

    @Text(
        name = "Score",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedScore = ""

    @Text(
        name = "Secrets",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedSecrets = ""

    @Text(
        name = "Crypts",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedCrypts = "C"

    @Text(
        name = "Mimic",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedMimic = "M"

    @Text(
        name = "Prince",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedPrince = "P"

    @Text(
        name = "Mimic Yes",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedMimicYes = "✔"

    @Text(
        name = "Mimic No",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedMimicNo = "✘"

    @Text(
        name = "Prince Yes",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedPrinceYes = "✔"

    @Text(
        name = "Prince No",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedPrinceNo = "✘"

    @Text(
        name = "Death",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedDeaths = "D"

    @Text(
        name = "Puzzles",
        category = "Placeholder",
        subcategory = "Minimized",
    )
    var textMinimizedPuzzles = "P"

    @Switch(
        name = "Show Team Info",
        description = "Shows team member secrets and room times at end of run. Requires a valid API key.",
        category = "Other Features",
    )
    override var teamInfo = false

    @Dropdown(
        name = "Wither Door ESP",
        description = "Boxes unopened wither doors.",
        category = "Wither Door",
        options = ["Off", "First", "All"],
    )
    override var witherDoorESP = 0

    @Color(
        name = "No Key Color",
        category = "Wither Door",
        allowAlpha = true,
    )
    var witherDoorNoKeyColor = OneColor(255, 0, 0)

    @Color(
        name = "Has Key Color",
        category = "Wither Door",
        allowAlpha = true,
    )
    var witherDoorKeyColor = OneColor(0, 255, 0)

    @Slider(
        name = "Door Outline Width",
        category = "Wither Door",
        min = 0f,
        max = 10f,
    )
    override var witherDoorOutlineWidth = 3f

    @Slider(
        name = "Door Outline Opacity",
        category = "Wither Door",
        min = 0F,
        max = 1F,
    )
    override var witherDoorOutline = 1f

    @Slider(
        name = "Door Fill Opacity",
        category = "Wither Door",
        min = 0F,
        max = 1F,
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

        @Transient
        @Header(
            text = "Notification On Prince Killed",
            size = 2,
            category = "Notification",
        )
        val headerOnPrinceKilled = false

        @Extract(
            category = "Notification",
        )
        override var onPrinceKilled: NotificationOptionImpl = NotificationOptionImpl()
    }

    @Extract(
        category = "Notification",
    )
    override var notification = NotificationImpl()
}
