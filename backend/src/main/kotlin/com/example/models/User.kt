package com.example.models

import java.util.UUID

data class User(
    val id: UUID,
    val name: String,
    val email: String,
    val steamId: String?
)
