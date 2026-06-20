package com.example.routes

import com.example.models.requests.LoginRequest
import com.example.models.requests.RefreshRequest
import com.example.models.requests.RegisterRequest
import com.example.services.AuthService
import io.ktor.server.application.*
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.*

fun Application.configureAuthRouting() {
    val authService = AuthService()

    routing {
        post("/auth/login") {
            val req = call.receive<LoginRequest>()
            val jwt = authService.login(req.login, req.password)
            call.respond(jwt)
        }
        post("/auth/register") {
            val req = call.receive<RegisterRequest>()
            val jwt = authService.register(req.name, req.email, req.password)
            call.respond(jwt)
        }
        post("/auth/refresh"){
            //check refresh token and send back new jwt if valid
            val req = call.receive<RefreshRequest>()
            val newAuth = authService.refresh(req.refreshToken, req.refreshTokenId)
            call.respond(newAuth)
        }

    }
}
