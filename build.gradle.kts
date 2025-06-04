
plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.ktor)
    kotlin("plugin.serialization") version "1.9.0"
}

group = "com.example"
version = "0.0.1"

application {
    mainClass = "io.ktor.server.netty.EngineMain"

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(libs.ktor.server.core)
    implementation(libs.ktor.server.content.negotiation)
    implementation(libs.ktor.server.netty)
    implementation(libs.logback.classic)
    implementation(libs.ktor.server.config.yaml)
    testImplementation(libs.ktor.server.test.host)
    testImplementation(libs.kotlin.test.junit)

    //implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:2.1.20")

    implementation("io.ktor:ktor-client-core:2.1.20")
    implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.3") // Required for JSON serialization

    //Firebase database
    implementation("com.google.firebase:firebase-admin:9.2.0")

    // Exposed ORM
    implementation("org.jetbrains.exposed:exposed-core:0.41.1")
    implementation("org.jetbrains.exposed:exposed-dao:0.41.1")
    implementation("org.jetbrains.exposed:exposed-jdbc:0.41.1")

    // MySQL
    implementation("mysql:mysql-connector-java:8.0.33") // For MySQL

}
