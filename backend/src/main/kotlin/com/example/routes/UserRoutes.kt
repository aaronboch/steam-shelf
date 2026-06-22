package com.example.routes

import com.example.models.requests.SteamIdRequest
import com.example.models.responses.UserProfile
import com.example.services.UserService
import io.ktor.server.application.Application
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.principal
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.get
import io.ktor.server.routing.post

import io.ktor.server.routing.put
import io.ktor.server.routing.routing


fun Application.configureUserRouting() {
    val userService = UserService()
    routing {
        get("/users/{name}") {
            val name = call.parameters["name"] ?: throw IllegalArgumentException("Missing userId parameter")
            val user = userService.getUserByName(name) ?: throw IllegalArgumentException("User not found")
            call.respond(UserProfile(user.id.toString(), user.name, user.steamId))
        }

        authenticate("auth-jwt") {
            put("/user/steam") {
                val userid =
                    call.principal<UserIdPrincipal>()?.name ?: throw IllegalArgumentException("Not authenticated")
                val req = call.receive<SteamIdRequest>()
                userService.setSteamId(userid, req.steamId)
                call.respond(mapOf("steamId" to req.steamId))
            }

            post("/users/{name}/games/refresh") {
                val name = call.parameters["name"] ?: throw IllegalArgumentException("Missing user name")
                val user = userService.getUserByName(name) ?: throw IllegalArgumentException("User not found")
                val steamId = user.steamId ?: throw IllegalArgumentException("User has no steamId")
                val games = userService.refreshGamesBySteamId(user, steamId)
                call.respond(games)
            }
        }
        get("/users/{name}/games") {
            val name = call.parameters["name"] ?: throw IllegalArgumentException("Missing user name")
            val user = userService.getUserByName(name) ?: throw IllegalArgumentException("User not found")
            val games = userService.getGamesResponsesByUserId(user.id)
            call.respond(games)
        }
    }
}