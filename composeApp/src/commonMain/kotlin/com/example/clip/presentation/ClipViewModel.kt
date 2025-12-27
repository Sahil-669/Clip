package com.example.clip.presentation

import com.example.clip.data.Clip
import com.example.clip.data.ClipDao
import com.example.clip.data.ClipboardSource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlin.time.Clock

class ClipViewModel (
    private val dao: ClipDao,
    private val clipboard: ClipboardSource
) {

    private val scope = CoroutineScope(Dispatchers.Main)

    val clips = dao.getAllClips()
        .stateIn(scope, SharingStarted.Lazily, emptyList())

    fun checkClipboard() {
        val realText = clipboard.getClipBoardText()

        if (realText != null) {
            scope.launch {
                val detectedType = determineClipType(realText)
                val clip = Clip(
                    id = realText.hashCode().toString(),
                    content = realText,
                    timestamp = Clock.System.now().toEpochMilliseconds(),
                    type = detectedType
                )
                dao.insert(clip)
            }
        }
    }

    fun deleteClip(clip: Clip) {
        scope.launch {
            dao.delete(clip)
        }
    }
}