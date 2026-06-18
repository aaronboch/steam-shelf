package com.example.models

import java.util.UUID

data class User(
    val id: UUID,
    val email: String,
    val name: String,
    val steamId: String?
)
