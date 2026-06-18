plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlin.serialization)
    application
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.netty)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.serialization.kotlinx.json)
    implementation(libs.ktor.server.config.yaml)
    implementation(libs.logback.classic)
    testImplementation(kotlin("test"))
    testImplementation(libs.ktor.server.test.host)
    implementation(libs.jbcrypt)
}

application {
    mainClass = "com.example.MainKt"
}

kotlin {
    jvmToolchain(21)
}

tasks.test {
    useJUnitPlatform()
}
