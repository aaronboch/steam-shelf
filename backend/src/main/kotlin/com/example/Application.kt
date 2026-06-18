package com.example

import com.example.routes.configureRouting
import io.ktor.server.application.*

fun Application.module() {
    configureRouting()
}
