package com.example

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import configureRouting
import io.ktor.server.application.Application
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import database.UsersSQl


fun main(args: Array<String>) {
    io.ktor.server.netty.EngineMain.main(args)
}

fun Application.module() {
    initFirebase()
    connectToDatabase()
    configureSerialization()
    configureRouting()
}

fun connectToDatabase() {
    Database.connect(
        url = "jdbc:mysql://localhost:3306/login_register",
        driver = "com.mysql.cj.jdbc.Driver",
        user = "root",
        password = "12345678"
    )
    transaction {
        SchemaUtils.create(UsersSQl)
    }
}

fun initFirebase() {
    val serviceAccount =
        Application::class.java.getResourceAsStream("/fir-demo-573d1-firebase-adminsdk-yrnrh-7bd81bbd5c.json")
            ?: throw IllegalStateException("Firebase service account file not found.")

    val options = FirebaseOptions.builder()
        .setCredentials(GoogleCredentials.fromStream(serviceAccount))
        .build()

    FirebaseApp.initializeApp(options)
}