package com.example.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class GameResponse(
    val appId: Int,
    val name: String,
    val capsule: String = "",
    val playtimeForever: Int,
    val playtime2weeks: Int,
    val playtimeWindowsForever: Int,
    val playtimeMacForever: Int,
    val playtimeLinuxForever: Int,
    val playtimeDeckForever: Int,
    val timeLastPlayed: Int,
    val playtimeDisconnected: Int,
    val lastFetched: String,
)