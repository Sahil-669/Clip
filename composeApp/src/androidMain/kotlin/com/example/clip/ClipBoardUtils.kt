package com.example.clip

import android.content.ClipDescription
import android.content.ClipboardManager
import android.content.Context

fun getClipBoard(context: Context): String? {
    val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager

    if (!clipboard.hasPrimaryClip()) return null

    if (clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN) == false &&
        clipboard.primaryClipDescription?.hasMimeType(ClipDescription.MIMETYPE_TEXT_HTML) == false) {
        return null
    }
    val item = clipboard.primaryClip?.getItemAt(0)
    return item?.text?.toString() ?: item?.coerceToText(context)?.toString()
}