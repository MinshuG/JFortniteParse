package me.fungames.jfortniteparse.ue4.versions

enum class Ue4Version(val game : Int) {
    GAME_UE4_0(GAME_UE4(0)),
    GAME_UE4_1(GAME_UE4(1)),
    GAME_UE4_2(GAME_UE4(2)),
    GAME_UE4_3(GAME_UE4(3)),
    GAME_UE4_4(GAME_UE4(4)),
    GAME_UE4_5(GAME_UE4(5)),
    GAME_UE4_6(GAME_UE4(6)),
    GAME_UE4_7(GAME_UE4(7)),
    GAME_UE4_8(GAME_UE4(8)),
    GAME_UE4_9(GAME_UE4(9)),
    GAME_UE4_10(GAME_UE4(10)),
    GAME_UE4_11(GAME_UE4(11)),
    GAME_UE4_12(GAME_UE4(12)),
    GAME_UE4_13(GAME_UE4(13)),
    GAME_UE4_14(GAME_UE4(14)),
    GAME_UE4_15(GAME_UE4(15)),
    GAME_UE4_16(GAME_UE4(16)),
    GAME_UE4_17(GAME_UE4(17)),
    GAME_UE4_18(GAME_UE4(18)),
    GAME_UE4_19(GAME_UE4(19)),
    GAME_UE4_20(GAME_UE4(20)),
    GAME_UE4_21(GAME_UE4(21)),
    GAME_UE4_22(GAME_UE4(22)),
    GAME_UE4_23(GAME_UE4(23)),
    GAME_UE4_24(GAME_UE4(24)),
    GAME_UE4_25(GAME_UE4(25)),
    GAME_UE4_26(GAME_UE4(26)),

    //CUSTOM GAMES
    GAME_VALORANT(me.fungames.jfortniteparse.ue4.versions.GAME_VALORANT),

    GAME_UE4_LATEST(GAME_UE4(LATEST_SUPPORTED_UE4_VERSION));

    val version = ue4Versions[GAME_UE4_GET_MINOR(game)]
}

const val GAME_UE4_BASE = 0x1000000

// bytes: 01.00.0N.NX : 01=UE4, 00=masked by GAME_ENGINE, NN=UE4 subversion, X=game (4 bits, 0=base engine)
// const val GAME_Borderlands3 = GAME_UE4(20) + 2
val GAME_VALORANT = GAME_UE4(22) + 1

fun GAME_UE4(x : Int) = GAME_UE4_BASE + (x shl 4)
fun GAME_UE4_GET_MINOR(x : Int) = (x - GAME_UE4_BASE) shr 4
fun GAME_UE4_GET_AR_VER(game : Int) = ue4Versions[GAME_UE4_GET_MINOR(game)]

const val LATEST_SUPPORTED_UE4_VERSION = 26

internal val ue4Versions = arrayOf(VER_UE4_0, VER_UE4_1, VER_UE4_2, VER_UE4_3, VER_UE4_4,
    VER_UE4_5, VER_UE4_6, VER_UE4_7, VER_UE4_8, VER_UE4_9,
    VER_UE4_10, VER_UE4_11, VER_UE4_12, VER_UE4_13, VER_UE4_14,
    VER_UE4_15, VER_UE4_16, VER_UE4_17, VER_UE4_18, VER_UE4_19,
    VER_UE4_20, VER_UE4_21, VER_UE4_22, VER_UE4_23, VER_UE4_24,
    VER_UE4_25, VER_UE4_26)