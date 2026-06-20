package com.example.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class SteamIdRequest(val steamId: String)
