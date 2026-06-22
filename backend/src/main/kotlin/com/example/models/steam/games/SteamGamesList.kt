package com.example.models.steam.games

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SteamGamesList(
    @SerialName("game_count") val gameCount: Int,
    val games: List<SteamGame>
)
