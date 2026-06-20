package com.example.repositories

import com.example.models.User
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

object UserRepository {
    private val users: ConcurrentHashMap<UUID, UserRow> = ConcurrentHashMap()

    data class UserRow(
        val user: User,
        val hashedPassword: String
    )

    fun findByEmail(email: String) = users.values.find { it.user.email == email }
    fun findByUsername(name: String) = users.values.find { it.user.name == name }
    fun findById(id: UUID) = users[id]
    fun save(row: UserRow) {
        users[row.user.id] = row
    }
}
