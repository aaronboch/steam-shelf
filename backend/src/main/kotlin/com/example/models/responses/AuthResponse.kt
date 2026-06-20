package com.example.models.responses

import kotlinx.serialization.Serializable

@Serializable
data class AuthResponse(
    val accessToken: String, val refreshToken: String, val refreshTokenId: String
)
