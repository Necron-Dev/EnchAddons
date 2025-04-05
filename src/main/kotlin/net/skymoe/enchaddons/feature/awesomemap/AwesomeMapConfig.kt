package net.skymoe.enchaddons.feature.awesomemap

import cc.polyfrost.oneconfig.config.core.OneColor
import net.skymoe.enchaddons.feature.config.FeatureConfig

interface AwesomeMapConfig : FeatureConfig {
    var autoScan: Boolean
    var scanChatInfo: Boolean

    var legitMode: Boolean
    var peekMode: Int

    var mapEnabled: Boolean
    var mapRotate: Boolean
    var mapCenter: Boolean
    var mapDynamicRotate: Boolean
    var mapHideInBoss: Boolean
    var playerHeads: Int
    var mapVanillaMarker: Boolean

    var mapX: Int
    var mapY: Int
    var mapScale: Float
    var textScale: Float
    var playerHeadScale: Float
    var playerNameScale: Float

    var mapBackground: OneColor
    var mapBorder: OneColor
    var mapBorderWidth: Float

    var mapDarkenUndiscovered: Boolean
    var mapDarkenPercent: Float
    var mapGrayUndiscovered: Boolean

    var mapRoomNames: Int
    var mapRoomSecrets: Int
    var mapCenterRoomName: Boolean
    var mapColorText: Boolean
    var mapCheckmark: Int
    var mapCenterCheckmark: Boolean

    var colorBloodDoor: OneColor
    var colorEntranceDoor: OneColor
    var colorRoomDoor: OneColor
    var colorWitherDoor: OneColor
    var colorOpenWitherDoor: OneColor
    var colorUnopenedDoor: OneColor

    var colorBlood: OneColor
    var colorEntrance: OneColor
    var colorFairy: OneColor
    var colorMiniboss: OneColor
    var colorRoom: OneColor
    var colorRoomMimic: OneColor
    var colorPuzzle: OneColor
    var colorRare: OneColor
    var colorTrap: OneColor
    var colorUnopened: OneColor

    var colorTextCleared: OneColor
    var colorTextUncleared: OneColor
    var colorTextGreen: OneColor
    var colorTextFailed: OneColor

    var scoreElementEnabled: Boolean
    var scoreAssumeSpirit: Boolean
    var scoreMinimizedName: Boolean
    var scoreHideInBoss: Boolean

    var scoreX: Int
    var scoreY: Int
    var scoreScale: Float

    var scoreTotalScore: Int
    var scoreSecrets: Int
    var scoreCrypts: Boolean
    var scoreMimic: Boolean
    var scoreDeaths: Boolean
    var scorePuzzles: Int
    var scoreMessage: Int
    var scoreTitle: Int
    var message270: String
    var message300: String
    var timeTo300: Boolean

    var mapShowRunInformation: Boolean
    var runInformationScore: Boolean
    var runInformationSecrets: Int
    var runInformationCrypts: Boolean
    var runInformationMimic: Boolean
    var runInformationDeaths: Boolean

    var apiKey: String
    var teamInfo: Boolean

    var mimicMessageEnabled: Boolean
    var mimicMessage: String
    var witherDoorESP: Int
    var witherDoorNoKeyColor: OneColor
    var witherDoorKeyColor: OneColor
    var witherDoorOutlineWidth: Float
    var witherDoorOutline: Float
    var witherDoorFill: Float
    var forceSkyblock: Boolean
    var paulBonus: Boolean
    var renderBeta: Boolean
    var customPrefix: String
}
