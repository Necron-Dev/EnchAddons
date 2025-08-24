package net.skymoe.enchaddons.feature.awesomemap

import net.skymoe.enchaddons.feature.config.FeatureConfig
import net.skymoe.enchaddons.feature.config.NotificationOption

interface AwesomeMapConfig : FeatureConfig {
    val autoScan: Boolean
    val scanChatInfo: Boolean

    val legitMode: Boolean

    val mapRotateMode: Int
    val mapCenter: Boolean
    val mapHideInBoss: Boolean
    val playerHeads: Int
    val mapVanillaMarker: Boolean
    val mapVanillaMarkerTeammates: Boolean
    val mapPlayerHeadColorBorder: Boolean
    val mapClip: Boolean

    val textScale: Float
    val vanillaMarkerScale: Float
    val colorBorderSize: Float
    val playerHeadScale: Float
    val playerNameScale: Float

    val mapDarkenUndiscovered: Boolean
    val mapDarkenPercent: Float
    val mapGrayUndiscovered: Boolean

    val mapRoomNames: Int
    val mapRoomSecrets: Int
    val mapCenterRoomName: Boolean
    val mapColorText: Boolean
    val mapCheckmark: Boolean
    val mapCenterCheckmark: Boolean

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
    val scorePrince: Boolean
    val scoreDeaths: Boolean
    val scorePuzzles: Int

    val mapShowRunInformation: Boolean
    val runInformationScore: Boolean
    val runInformationSecrets: Int
    val runInformationCrypts: Boolean
    val runInformationMimic: Boolean
    val runInformationPrince: Boolean
    val runInformationDeaths: Boolean

    val teamInfo: Boolean

    val witherDoorESP: Int
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
    val onPrinceKilled: NotificationOption
}
