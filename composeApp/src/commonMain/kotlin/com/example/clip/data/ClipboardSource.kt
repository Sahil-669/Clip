package com.example.clip.data

interface ClipboardSource {
    fun getClipBoardText(): String?
    fun getClipBoardImage() : String?
}