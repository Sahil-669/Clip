package com.example.clip.presentation

import androidx.compose.ui.graphics.Color
import com.example.clip.data.ClipType

fun determineClipType(content: String): ClipType {

    val trimmed = content.trim()
    return when {
        content.startsWith("/") || content.startsWith("file://") -> ClipType.IMAGE
        trimmed.startsWith("http", ignoreCase = true) || trimmed.startsWith("www.", ignoreCase = true) -> ClipType.LINK
        trimmed.matches(Regex("^#([A-Fa-f0-9]{6})$")) -> ClipType.COLOR
        isLikelyCode(trimmed) -> ClipType.CODE
        else -> ClipType.TEXT
    }
}

fun isLikelyCode(text: String): Boolean {

    val keyWords = listOf(
        "class", "fun", "val ", "var ", "import ", "package ",
        "void ", "int ", "return ", "def ", "function", "const ",
        "<html>", "div", "span"
    )
    val hasKeywords = keyWords.any { text.contains(it) }
    val hasBrackets = text.contains("{") && text.contains("}")
    val isMultiLine = text.lines().size > 1

    return (hasKeywords && (hasBrackets || isMultiLine))
}
fun getHeaderColor(type: ClipType): Color {
    return when(type) {
        ClipType.COLOR -> Color(0xFFA855F7)
        ClipType.LINK -> Color(0xFF3B82F6)
        ClipType.CODE -> Color(0xFF374151)
        ClipType.IMAGE -> Color(0xFFEF4444)
        ClipType.TEXT -> Color(0xFF10B981)
    }
}

fun getClipTitle(type: ClipType): String {
    return when (type) {
        ClipType.COLOR -> "Color"
        ClipType.LINK -> "Link"
        ClipType.CODE -> "Code"
        ClipType.IMAGE -> "Image"
        ClipType.TEXT -> "Text"
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