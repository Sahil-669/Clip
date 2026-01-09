package com.example.clip.data

import android.content.ClipboardManager
import android.content.Context
import com.example.clip.getClipBoard
import java.io.File
import java.util.UUID

class AndroidClipboardSource (private val context: Context): ClipboardSource {
    override fun getClipBoardText(): String? {
        return getClipBoard(context)
    }

    override fun getClipBoardImage(): String? {
        val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        if (!clipboard.hasPrimaryClip()) return null

        val item = clipboard.primaryClip?.getItemAt(0)
        val uri = item?.uri?: return null

        return try {
            val inputStream = context.contentResolver.openInputStream(uri)
            val fileName = "clip_${UUID.randomUUID()}.jpg"
            val file = File(context.filesDir, fileName)

            inputStream?.use { input ->
                file.outputStream().use {output ->
                    input.copyTo(output)
                }
            }
            file.absolutePath
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }
}