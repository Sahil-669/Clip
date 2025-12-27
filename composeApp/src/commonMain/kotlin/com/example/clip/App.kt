package com.example.clip


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Link
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.clip.data.Clip
import com.example.clip.data.ClipType
import com.example.clip.presentation.ClipViewModel
import com.example.clip.presentation.parseHexColor
import org.koin.compose.koinInject

@Composable
fun App() {
    val viewModel = koinInject<ClipViewModel>()

    val clips by viewModel.clips.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = {viewModel.checkClipboard()}) {
                Icon(Icons.Default.Add, contentDescription = "add")
            }
        }
    ) { paddingValues ->
        if (clips.isEmpty()) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("No Clips")
            }
        } else {
            LazyColumn (
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(clips) { clip ->
                    ClipRow(
                        clip = clip,
                        onDelete = { viewModel.deleteClip(clip) }
                    )
                }
            }
        }
    }
}

@Composable
fun ClipRow(clip: Clip, onDelete: () -> Unit) {
    Card (
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ){
            when(clip.type) {
                ClipType.LINK -> {
                    Icon(Icons.Default.Link, contentDescription = "Link", tint = Color.Blue)
                }
                ClipType.COLOR -> {
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                            .background(parseHexColor(clip.content))
                    )
                }
                else -> {
                    Icon(Icons.Default.Description, contentDescription = "Text", tint = Color.Gray)
                }
            }
            Text(
                text = clip.content,
                modifier = Modifier.weight(1f),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red.copy(0.6f))
            }
        }
    }
}