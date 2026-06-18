package com.example

import com.example.routes.configureAuthRouting
import com.example.routes.configureRouting
import io.ktor.serialization.kotlinx.json.json
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation

fun Application.module() {
    install(ContentNegotiation) {
        json()
    }
    configureRouting()
    configureAuthRouting()
}
