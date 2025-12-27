package com.example.clip.presentation

import androidx.compose.ui.graphics.Color
import com.example.clip.data.ClipType

fun determineClipType(content: String): ClipType {

    val trimmed = content.trim()
    return when {
        trimmed.startsWith("http", ignoreCase = true) || trimmed.startsWith("www.", ignoreCase = true) -> ClipType.LINK
        trimmed.matches(Regex("^#([A-Fa-f0-9]{6})$")) -> ClipType.COLOR
        else -> ClipType.TEXT
    }
}

fun parseHexColor(hex: String): Color {
    return try {
        val clean = hex.removePrefix("#")
        val colorLong = clean.toLong(16) or 0xFF000000
        Color(colorLong)
    } catch (_: Exception) {
        Color.Gray
    }
}