package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.routes.configureAuthRouting
import com.example.routes.configureRouting
import io.github.cdimascio.dotenv.dotenv
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.auth.Authentication
import io.ktor.server.auth.UserIdPrincipal
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    install(Authentication) {
        jwt("auth-jwt") {
            realm = "steam-shelf"
            val dotenv = dotenv()
            val secret = dotenv["JWT_SECRET"] ?: throw IllegalStateException("JWT_SECRET is not set")
            verifier(JWT.require(Algorithm.HMAC256(secret)).build())
            validate { credential ->
                val userId = credential.payload.subject
                if (userId != null) UserIdPrincipal(userId) else null
            }
        }
    }

    install(StatusPages) {
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, mapOf("error" to cause.message))
        }
    }

    configureRouting()
    configureAuthRouting()
}
