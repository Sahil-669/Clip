package com.example.clip.data

import platform.UIKit.UIPasteboard

class IosClipboardSource: ClipboardSource {
    override fun getClipBoardText(): String? {
        return UIPasteboard.generalPasteboard.string
    }
}