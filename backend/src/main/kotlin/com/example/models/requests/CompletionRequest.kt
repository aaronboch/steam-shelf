package com.example.models.requests

import kotlinx.serialization.Serializable

@Serializable
data class CompletionRequest(val completed: Boolean)
