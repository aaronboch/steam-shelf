package com.example.models.steam.stats

import kotlinx.serialization.Serializable

@Serializable
data class SteamUserStats(val playerstats: SteamPlayerStats)
