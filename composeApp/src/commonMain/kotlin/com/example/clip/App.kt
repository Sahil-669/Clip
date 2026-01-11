package com.example.clip

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Code
import androidx.compose.material.icons.filled.Colorize
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Image
import androidx.compose.material.icons.filled.Link
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.example.clip.data.Clip
import com.example.clip.data.ClipType
import com.example.clip.presentation.ClipViewModel
import com.example.clip.presentation.getClipTitle
import com.example.clip.presentation.getHeaderColor
import com.example.clip.presentation.parseHexColor
import org.koin.compose.koinInject

@Composable
fun App() {
    val viewModel = koinInject<ClipViewModel>()

    val clips by viewModel.clips.collectAsState()
    val isSystemDark = androidx.compose.foundation.isSystemInDarkTheme()
    val colorScheme = if (isSystemDark)
        androidx.compose.material3.darkColorScheme()
        else
        androidx.compose.material3.lightColorScheme()

    MaterialTheme(colorScheme = colorScheme) {
    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
            floatingActionButton = {
                FloatingActionButton(
                    onClick = { viewModel.checkClipboard() }
                ) {
                    Icon(Icons.Default.Refresh, contentDescription = "Refresh")
                }
            }
        ) { paddingValues ->
            if (clips.isEmpty()) {
                Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No Clips!", color = MaterialTheme.colorScheme.onBackground)
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(
                        items = clips,
                        key = { it.id }
                    ) { clip ->
                        var isDismissed by remember { mutableStateOf(false) }
                        val dismissState = rememberSwipeToDismissBoxState(
                            positionalThreshold = { totalDistance -> totalDistance * 1f }
                        )
                        LaunchedEffect(dismissState) {
                            snapshotFlow { dismissState.progress to dismissState.currentValue }
                                .collect { (progress, value) ->
                                    if (value == SwipeToDismissBoxValue.EndToStart && progress >= 0.98f) {
                                        isDismissed = true
                                    }
                                }
                        }
                        LaunchedEffect(isDismissed) {
                            if (isDismissed) {
                                kotlinx.coroutines.delay(400)
                                viewModel.deleteClip(clip)
                            }
                        }
                        androidx.compose.animation.AnimatedVisibility(
                            visible = !isDismissed,
                            exit = androidx.compose.animation.shrinkHorizontally(
                                animationSpec = androidx.compose.animation.core.tween(500)
                            ) + androidx.compose.animation.fadeOut()
                        ) {
                            SwipeToDismissBox(
                                state = dismissState,
                                modifier = Modifier.animateItem(),
                                enableDismissFromStartToEnd = false,
                                enableDismissFromEndToStart = true,
                                backgroundContent = {
                                    val backgroundColor by animateColorAsState(
                                        targetValue = when (dismissState.targetValue) {
                                            SwipeToDismissBoxValue.EndToStart -> Color.Red
                                            else -> Color(0xFFFF1744)
                                        },
                                        label = "BgColor"
                                    )
                                    val iconScale by animateFloatAsState(
                                        targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart) 1.2f else 0.8f,
                                        label = "IconAnim"
                                    )
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(backgroundColor, RoundedCornerShape(12.dp))
                                            .padding(horizontal = 20.dp),
                                        contentAlignment = Alignment.CenterEnd
                                    ) {
                                        val currentOffset = try {
                                            dismissState.requireOffset()
                                        } catch (_: Exception) {0f}
                                        val iconOffset = if (currentOffset < 0) {
                                            (currentOffset / 2).toInt()
                                        } else 0
                                        if (dismissState.progress > 0.15f) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "delete",
                                                tint = Color.White,
                                                modifier = Modifier
                                                    .offset { IntOffset(x = iconOffset, y = 0) }
                                                    .scale(iconScale)
                                            )
                                        }
                                    }
                                },
                                content = {
                                    ClipItem(
                                        clip = clip,
                                        onDelete = { viewModel.deleteClip(clip) }
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ClipItem(clip: Clip, onDelete: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    val cardModifier = if (clip.type == ClipType.LINK) {
        Modifier.clickable {
            try { uriHandler.openUri(clip.content) } catch (e: Exception) { e.printStackTrace() }
        }
    } else Modifier
    CardBase(
        clip = clip,
        onDelete = onDelete,
        modifier = cardModifier
    ) {
        when (clip.type) {
            ClipType.COLOR -> ColorContent(clip.content)
            ClipType.LINK -> TextContent(clip.content, isLink = true)
            ClipType.CODE -> CodeContent(clip.content)
            ClipType.IMAGE -> ImageContent(clip.content)
            else -> TextContent(clip.content, isLink = false)
        }
    }
}



@Composable
fun CardBase(
    clip: Clip,
    onDelete: () -> Unit,
    modifier: Modifier,
    content: @Composable () -> Unit,
) {
    val headerColor = getHeaderColor(clip.type)
    val title = getClipTitle(clip.type)

    val icon: ImageVector = when(clip.type) {
        ClipType.COLOR -> Icons.Default.Colorize
        ClipType.LINK -> Icons.Default.Link
        ClipType.IMAGE -> Icons.Default.Image
        ClipType.TEXT -> Icons.Default.Description
        ClipType.CODE -> Icons.Filled.Code
    }

    Card (
        modifier = modifier.fillMaxWidth().height(160.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1E1E))
    ) {
        Column {
            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .background(headerColor)
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = title,
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
                IconButton(
                    onClick = onDelete,
                    modifier = Modifier.size(24.dp)
                ) {
                    Icon(
                        Icons.Default.Close,
                        contentDescription = "Delete",
                        tint = Color.White.copy(alpha = 0.7f)
                    )
                }

            }
            Box(
                modifier = Modifier.fillMaxSize().background(Color(0xFF252525))
            ) {
                content()
            }
        }
    }
}

@Composable
fun ColorContent(hexContent: String) {
    val color = parseHexColor(hexContent)

    Box(modifier = Modifier.fillMaxSize()) {

        Box(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight()
                .background(color)
        )

        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .offset(y = 20.dp)
                .clip(RoundedCornerShape(50))
                .background(Color.Black)
                .padding(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text(
                text = hexContent.uppercase(),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
            )
        }
    }
}

@Composable
fun TextContent(text: String, isLink: Boolean) {
    Box(modifier = Modifier.padding(16.dp)) {
        Text(
            text = text,
            color = if (isLink) Color(0xFF60A5FA) else Color.White,
            fontSize = 14.sp,
            lineHeight = 20.sp,
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        )
    }
}

@Composable
fun CodeContent(code: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2B2B))
            .padding(12.dp)
            .verticalScroll(rememberScrollState())
    ) {
        Text(
            text = code,
            color = Color(0xFFA9B7C6),
            fontFamily = FontFamily.Monospace,
            fontSize = 12.sp
        )
    }
}

@Composable
fun ImageContent(path: String) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        AsyncImage(
            model = path,
            contentDescription = "Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }

}