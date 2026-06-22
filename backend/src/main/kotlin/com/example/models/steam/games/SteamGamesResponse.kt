package com.example.models.steam.games

import kotlinx.serialization.Serializable

@Serializable
data class SteamGamesResponse(val response: SteamGamesList) {

}