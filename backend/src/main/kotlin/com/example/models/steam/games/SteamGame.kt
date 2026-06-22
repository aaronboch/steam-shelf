package com.example.models.steam.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamGame(
    val appid: Int,
    val name: String,
    @SerialName("playtime_forever") val playtimeForever: Int,
    @SerialName("playtime_2weeks") val playtime2weeks: Int = 0,
    @SerialName("playtime_windows_forever") val playtimeWindowsForever: Int,
    @SerialName("playtime_mac_forever") val playtimeMacForever: Int,
    @SerialName("playtime_linux_forever") val playtimeLinuxForever: Int,
    @SerialName("playtime_deck_forever") val playtimeDeckForever: Int,
    @SerialName("rtime_last_played") val timeLastPlayed: Int,
    @SerialName("playtime_disconnected") val playtimeDisconnected: Int,
    @SerialName("capsule_filename") val capsuleFilename: String = "")

