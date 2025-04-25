package net.skymoe.enchaddons.feature.awesomemap

import cc.polyfrost.oneconfig.config.core.OneColor
import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.config.NotificationOption

interface AwesomeMapConfig : FeatureConfig {
    val autoScan: Boolean
    val scanChatInfo: Boolean

    val legitMode: Boolean

    val mapRotate: Boolean
    val mapCenter: Boolean
    val mapDynamicRotate: Boolean
    val mapHideInBoss: Boolean
    val playerHeads: Int
    val mapVanillaMarker: Boolean

    val textScale: Float
    val playerHeadScale: Float
    val playerNameScale: Float

    val mapDarkenUndiscovered: Boolean
    val mapDarkenPercent: Float
    val mapGrayUndiscovered: Boolean

    val mapRoomNames: Int
    val mapRoomSecrets: Int
    val mapCenterRoomName: Boolean
    val mapColorText: Boolean
    val mapCheckmark: Int
    val mapCenterCheckmark: Boolean

    val colorBloodDoor: OneColor
    val colorEntranceDoor: OneColor
    val colorRoomDoor: OneColor
    val colorWitherDoor: OneColor
    val colorOpenWitherDoor: OneColor
    val colorUnopenedDoor: OneColor

    val colorBlood: OneColor
    val colorEntrance: OneColor
    val colorFairy: OneColor
    val colorMiniboss: OneColor
    val colorRoom: OneColor
    val colorRoomMimic: OneColor
    val colorPuzzle: OneColor
    val colorRare: OneColor
    val colorTrap: OneColor
    val colorUnopened: OneColor

    val colorTextCleared: OneColor
    val colorTextUncleared: OneColor
    val colorTextGreen: OneColor
    val colorTextFailed: OneColor

    val scoreElementEnabled: Boolean
    val scoreAssumeSpirit: Boolean
    val scoreMinimizedName: Boolean
    val scoreHideInBoss: Boolean

    val scoreX: Int
    val scoreY: Int
    val scoreScale: Float

    val scoreTotalScore: Int
    val scoreSecrets: Int
    val scoreCrypts: Boolean
    val scoreMimic: Boolean
    val scoreDeaths: Boolean
    val scorePuzzles: Int

    val mapShowRunInformation: Boolean
    val runInformationScore: Boolean
    val runInformationSecrets: Int
    val runInformationCrypts: Boolean
    val runInformationMimic: Boolean
    val runInformationDeaths: Boolean

    val apiKey: String
    val teamInfo: Boolean

    val mimicMessageEnabled: Boolean
    val mimicMessage: String
    val witherDoorESP: Int
    val witherDoorNoKeyColor: OneColor
    val witherDoorKeyColor: OneColor
    val witherDoorOutlineWidth: Float
    val witherDoorOutline: Float
    val witherDoorFill: Float
    val forceSkyblock: Boolean
    val paulBonus: Boolean
    val renderBeta: Boolean
    val customPrefix: String

    val notification: Notification
}

interface Notification {
    val onScore270: NotificationOption
    val onScore300: NotificationOption
    val onMimicKilled: NotificationOption
}
