package com.example.services

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.models.User
import com.example.models.responses.AuthResponse
import io.github.cdimascio.dotenv.dotenv
import org.mindrot.jbcrypt.BCrypt
import java.util.Date
import com.example.models.RefreshToken
import com.example.repositories.RefreshTokenRepository
import com.example.repositories.UserRepository
import java.security.MessageDigest
import java.security.SecureRandom
import java.util.UUID
import kotlin.time.Clock
import kotlin.time.Duration.Companion.days

class AuthService() {
    val refreshTokenRepo = RefreshTokenRepository()
    val dotenv = dotenv()
    val random = SecureRandom()

    fun createJwtToken(user: User): String {
        val secret = dotenv["JWT_SECRET"] ?: throw IllegalStateException("JWT_SECRET not set in .env")
        return JWT.create()
            .withSubject(user.id.toString())
            .withClaim("name", user.name)
            .withClaim("email", user.email)
            .withClaim("steamId", user.steamId ?: "")
            .withIssuedAt(Date())
            .withExpiresAt(Date(System.currentTimeMillis() + 3600_000)) // 1 hour
            .sign(Algorithm.HMAC256(secret))
    }

    fun createAuthResponse(user: User): AuthResponse {
        val jwt = createJwtToken(user)

        val rnd = ByteArray(64)
        random.nextBytes(rnd)
        val rawToken = rnd.joinToString("") { "%02x".format(it) }
        val tokenHash = MessageDigest.getInstance("SHA-256").digest(rawToken.toByteArray()).joinToString("") { "%02x".format(it) }
        val refreshToken = RefreshToken(
            UUID.randomUUID(), user.id, tokenHash,
            Clock.System.now().plus(7.days),
            revoked = false,
        )
        refreshTokenRepo.save(refreshToken)
        return AuthResponse(jwt, rawToken, refreshToken.id.toString())
    }


    fun register(name: String, email: String, password: String): AuthResponse {
        if (UserRepository.findByEmail(email) != null) throw IllegalArgumentException("Email already in use")
        if (UserRepository.findByUsername(name) != null) throw IllegalArgumentException("Username already in use")

        val hash = hashPassword(password)
        val user = User(
            name = name,
            email = email,
            id = UUID.randomUUID(),
            steamId = null,
        )
        UserRepository.save(UserRepository.UserRow(user = user, hashedPassword = hash))


        return createAuthResponse(user)
    }

    fun login(login: String, password: String): AuthResponse {
        val userRow = if (login.contains('@')) {
            UserRepository.findByEmail(login)
        } else {
            UserRepository.findByUsername(login)
        } ?: throw IllegalArgumentException("User not found")

        if (!verifyPassword(password, userRow.hashedPassword)) throw IllegalArgumentException("Passwords do not match")
        val user = userRow.user

        return createAuthResponse(user)
    }

    fun refresh(refreshToken: String, refreshTokenId: String): AuthResponse {
        val hash = MessageDigest.getInstance("SHA-256").digest(refreshToken.toByteArray())
            .joinToString("") { "%02x".format(it) }
        val token = refreshTokenRepo.findByHash(hash) ?: throw IllegalArgumentException("Refresh token not found")
        if (token.id != UUID.fromString(refreshTokenId)) throw IllegalArgumentException("Refresh token ID does not match")
        if (token.revoked) throw IllegalArgumentException("Refresh token revoked")
        if (token.expiresAt < Clock.System.now()) throw IllegalArgumentException("Refresh token expired")
        token.revoked = true
        val userRow = UserRepository.findById(token.userId) ?: throw IllegalArgumentException("User not found")
        val user = userRow.user

        return createAuthResponse(user)
    }

    fun hashPassword(password: String): String {
        return BCrypt.hashpw(password, BCrypt.gensalt())
    }

    fun verifyPassword(password: String, hashed: String): Boolean {
        return BCrypt.checkpw(password, hashed)
    }
}
