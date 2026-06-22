package com.example.services

import com.example.models.User
import com.example.repositories.UserRepository
import io.github.cdimascio.dotenv.dotenv
import java.net.URI
import java.util.UUID

class UserService() {
    val dotenv = dotenv()

    fun setSteamId(userId: String, steamId: String) {
        val userRow =
            UserRepository.findById(UUID.fromString(userId)) ?: throw IllegalArgumentException("User not found")
        val updatedUser = userRow.user.copy(steamId = steamId)
        UserRepository.save(UserRepository.UserRow(updatedUser, userRow.hashedPassword))
    }

    fun getUserByName(name: String): User? {
        val userRow = UserRepository.findByUsername(name) ?: return null
        return userRow.user
    }

    fun getGamesBySteamId(steamId: String): String{
        val apiKey = dotenv["STEAM_API_KEY"] ?: throw IllegalStateException("API key not set")
        val url =
            URI.create("https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=$apiKey&steamid=$steamId&format=json")
                .toURL()
        try {
            val response = url.openStream().bufferedReader().use { stream -> stream.readText() }
            return response
        } catch (e: Exception) {
            throw IllegalArgumentException("Steam unavailable right now. Try again later.")
        }
    }
}