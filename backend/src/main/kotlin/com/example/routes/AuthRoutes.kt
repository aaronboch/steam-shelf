package com.example.routes

import io.ktor.server.application.*
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureAuthRouting() {
    routing {
        route("/auth/login") {}
        route("/auth/register") {}
    }
}
