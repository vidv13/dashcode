package com.vidv13.dashcode.ui.detail

import android.app.Activity
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material3.AmbientAware
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Text

@Composable
fun QrCodeDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: QrCodeDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    KeepScreenOn()

    AmbientAware { ambientState ->
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(if (ambientState.isAmbient) Color.Black else Color.White),
            contentAlignment = Alignment.Center,
        ) {
            if (state.bitmap != null) {
                if (ambientState.isAmbient) {
                    AmbientQrContent(bitmap = state.bitmap!!, name = state.name)
                } else {
                    QrContent(name = state.name, bitmap = state.bitmap!!)
                }
            } else if (!ambientState.isAmbient) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun QrContent(name: String, bitmap: Bitmap) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = name,
            color = Color.Black,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(bottom = 4.dp),
        )
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = name,
            modifier = Modifier.size(180.dp),
        )
    }
}

@Composable
private fun AmbientQrContent(bitmap: Bitmap, name: String) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = name,
        modifier = Modifier.size(180.dp),
    )
}

@Composable
private fun KeepScreenOn() {
    val activity = LocalContext.current as? Activity ?: return
    DisposableEffect(Unit) {
        activity.window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        onDispose {
            activity.window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        }
    }
}
