package com.example.repositories

import com.example.models.RefreshToken
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

class RefreshTokenRepository {
    private val tokens: ConcurrentHashMap<UUID, RefreshToken> = ConcurrentHashMap()

    fun findByHash(hash: String) = tokens.values.find { it.tokenHash == hash }
    fun findAllByUserId(userId: UUID) = tokens.values.filter { it.userId == userId } // not sure how to do that
    fun findById(id: UUID) = tokens[id]
    fun save(token: RefreshToken) {
        tokens[token.id] = token
    }
}