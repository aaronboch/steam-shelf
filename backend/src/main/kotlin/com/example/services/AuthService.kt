package com.example.services

import com.example.models.User
import com.example.repositories.UserRepository
import org.mindrot.jbcrypt.BCrypt


class AuthService(private val userRepo: UserRepository = UserRepository()) {

    suspend fun register(name: String, email: String, password: String): String {
        if (userRepo.findByEmail(email) != null) throw IllegalArgumentException("Email already in use")

        val hash = hashPassword(password)
        val user = User(
            name = name,
            email = email,
            id = java.util.UUID.randomUUID(),
            steamId = null,
        )
        userRepo.save(UserRepository.UserRow(user = user, hashedPassword = hash))
        //TODO: return real jwt
        return "jwt-${user.id}"
    }

    suspend fun login(login: String, password: String): String {
        val user = if (login.contains('@')) {
            userRepo.findByEmail(login)
        } else {
            userRepo.findByUsername(login)
        } ?: throw IllegalArgumentException("User not found")

        if(!verifyPassword(password, user.hashedPassword)) throw IllegalArgumentException("Passwords do not match")

        //TODO: return real jwt
        return "jwt-${user.user.id}"
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}
