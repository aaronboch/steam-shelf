package com.example.models.steam.stats

import kotlinx.serialization.Serializable

@Serializable
data class SteamPlayerStats(val steamID: String, val gameName: String, val achievements: List<SteamAchievement>?, val stats: List<SteamStat>?)
