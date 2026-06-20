package com.example.models

import java.util.UUID
import kotlin.time.Instant

data class RefreshToken(
    val id: UUID,
    val userId: UUID,
    val tokenHash: String,
    val expiresAt: Instant,
    var revoked: Boolean
)
