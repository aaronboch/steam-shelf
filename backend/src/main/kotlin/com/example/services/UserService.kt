package com.example.services

import com.example.models.User
import com.example.models.responses.GameResponse
import com.example.models.steam.games.SteamGamesResponse
import com.example.repositories.GamesRepository
import com.example.repositories.UserGamesRepository
import com.example.repositories.UserRepository
import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.dotenv
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.json.Json
import java.net.URI
import java.util.UUID
import kotlin.time.Clock

class UserService(
    private val gamesRepository: GamesRepository = GamesRepository(),
    private val userGamesRepo: UserGamesRepository = UserGamesRepository(),
    private val dotenv: Dotenv = dotenv(),
    private val httpClient: HttpClient = HttpClient()
) {
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

    suspend fun refreshGamesBySteamId(user: User, steamId: String): List<GameResponse> {
        val apiKey = dotenv["STEAM_API_KEY"] ?: throw IllegalStateException("API key not set")
        val url =
            URI.create("https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=$apiKey&steamid=$steamId&format=json&include_appinfo=true&include_played_free_games=true&include_free_sub=true&include_extended_appinfo=true") //
                .toURL()
        val json = Json { ignoreUnknownKeys = true }
        try {
            val response = httpClient.get(url).bodyAsText()
            val parsed = json.decodeFromString<SteamGamesResponse>(response)
            parsed.response.games.forEach { game ->
                if (gamesRepository.findByAppId(game.appid) == null) {
                    gamesRepository.save(GamesRepository.GamesRow(game.appid, game.name, game.capsuleFilename))
                }
                userGamesRepo.save(
                    UserGamesRepository.UserGamesRow(
                        userId = user.id,
                        appId = game.appid,
                        playtime2weeks = game.playtime2weeks,
                        playtimeForever = game.playtimeForever,
                        playtimeWindowsForever = game.playtimeWindowsForever,
                        playtimeMacForever = game.playtimeMacForever,
                        playtimeLinuxForever = game.playtimeLinuxForever,
                        playtimeDeckForever = game.playtimeDeckForever,
                        timeLastPlayed = game.timeLastPlayed,
                        playtimeDisconnected = game.playtimeDisconnected,
                        lastFetched = Clock.System.now().toString()
                    )
                )
            }
            return getGamesResponsesByUserId(user.id)
        } catch (e: Exception) {
            e.printStackTrace()
            throw IllegalArgumentException("Steam unavailable right now. Try again later.")
        }
    }

    fun getGamesResponsesByUserId(userId: UUID): List<GameResponse> {
        return userGamesRepo.getAllByUserId(userId).map { row ->
            val game = gamesRepository.findByAppId(row.appId) ?: throw IllegalArgumentException("App not found")
            GameResponse(
                appId = row.appId,
                name = game.name,
                playtime2weeks = row.playtime2weeks,
                playtimeForever = row.playtimeForever,
                playtimeWindowsForever = row.playtimeWindowsForever,
                playtimeMacForever = row.playtimeMacForever,
                playtimeLinuxForever = row.playtimeLinuxForever,
                playtimeDeckForever = row.playtimeDeckForever,
                timeLastPlayed = row.timeLastPlayed,
                playtimeDisconnected = row.playtimeDisconnected,
                lastFetched = row.lastFetched,
                capsule = game.capsule,
            )
        }
    }
}