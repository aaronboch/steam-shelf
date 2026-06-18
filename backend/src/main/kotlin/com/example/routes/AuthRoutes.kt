package com.example.routes

import com.example.models.requests.LoginRequest
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
    }
}
