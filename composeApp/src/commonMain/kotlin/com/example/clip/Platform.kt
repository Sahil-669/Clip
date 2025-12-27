package com.example.clip

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform