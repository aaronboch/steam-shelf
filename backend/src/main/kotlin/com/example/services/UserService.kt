package com.example.services

import com.example.models.User
import com.example.repositories.UserRepository
import java.util.UUID

class UserService() {
    fun setSteamId(userId: String, steamId: String) {
        val userRow = UserRepository.findById(UUID.fromString(userId)) ?: throw IllegalArgumentException("User not found")
        val updatedUser = userRow.user.copy(steamId = steamId)
        UserRepository.save(UserRepository.UserRow(updatedUser, userRow.hashedPassword))
    }

    fun getUserByName(name: String): User? {
        val userRow = UserRepository.findByUsername(name) ?: return null
        return userRow.user
    }
}