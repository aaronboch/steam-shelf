package com.example.models.steam.stats

import kotlinx.serialization.Serializable

@Serializable
data class SteamStat(val name: String, val value: Int)
