package com.example.models.steam.stats

import kotlinx.serialization.Serializable

@Serializable
data class SteamAchievement(val apiname: String, val achieved: Int, val unlocktime: Long?)