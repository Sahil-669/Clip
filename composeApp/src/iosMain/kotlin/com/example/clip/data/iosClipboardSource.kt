package com.example.clip.data

import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUUID
import platform.Foundation.NSUserDomainMask
import platform.Foundation.writeToURL
import platform.UIKit.UIImageJPEGRepresentation
import platform.UIKit.UIPasteboard

class IosClipboardSource: ClipboardSource {
    override fun getClipBoardText(): String? {
        return UIPasteboard.generalPasteboard.string
    }

    override fun getClipBoardImage(): String? {
        val image = UIPasteboard.generalPasteboard.image ?: return null
        val data = UIImageJPEGRepresentation(image, 0.8) ?: return null
        val fileName = "clip_${NSUUID.UUID().UUIDString}.jpg"

        val fileManager = NSFileManager.defaultManager
        val urls = fileManager.URLsForDirectory(NSDocumentDirectory, NSUserDomainMask)
        val docDir = urls.first() as? NSURL
        val fileUrl = docDir?.URLByAppendingPathComponent(fileName)
        data.writeToURL(fileUrl!!, true)

        return fileUrl.path
    }
}