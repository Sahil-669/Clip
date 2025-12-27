package com.example.clip.data

import android.content.Context
import com.example.clip.getClipBoard

class AndroidClipboardSource (private val context: Context): ClipboardSource {
    override fun getClipBoardText(): String? {
        return getClipBoard(context)
    }
}