package com.example.repositories


import java.util.concurrent.ConcurrentHashMap

class GamesRepository {
    private val games: ConcurrentHashMap<Int, GamesRow> = ConcurrentHashMap()

    data class GamesRow(val appId: Int, val name: String, val capsule: String = "", var priceCents: Int? = null)

    fun findByAppId(appId: Int): GamesRow? {
        return games[appId]
    }
    fun save(game: GamesRow) {
        games[game.appId] = game
    }

    fun addPrice(appId: Int, price: Int?) {
        games[appId]?.priceCents = price
    }
}

