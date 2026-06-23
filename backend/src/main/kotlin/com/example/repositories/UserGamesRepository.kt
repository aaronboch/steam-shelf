package com.example.repositories

import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class UserGamesRepository {
    private val userGames: ConcurrentHashMap<UUID, MutableList<UserGamesRow>> = ConcurrentHashMap()

    data class UserGamesRow(
        val userId: UUID,
        val appId: Int,
        val playtime2weeks: Int,
        val playtimeForever: Int,
        val playtimeWindowsForever: Int,
        val playtimeMacForever: Int,
        val playtimeLinuxForever: Int,
        val playtimeDeckForever: Int,
        val timeLastPlayed: Int,
        val playtimeDisconnected: Int,
        val lastFetched: String,
        var completed: Boolean = false
    )

    fun save(row: UserGamesRow) {
        val games = userGames.getOrPut(row.userId, { mutableListOf() })
        val index = games.indexOfFirst { it.appId == row.appId }
        if (index >= 0) {
            games[index] = row.copy(completed = games[index].completed)
        } else {
            games.add(row)
        }
    }

    fun getAllByUserId(userId: UUID): List<UserGamesRow> {
        return userGames[userId] ?: emptyList()
    }

    fun complete(userId: UUID, appId: Int, completion: Boolean) {
        val games = userGames[userId] ?: return
        val index = games.indexOfFirst { it.appId == appId }
        if (index >= 0) {
            games[index] = games[index].copy(completed = completion)
        }

    }
}
