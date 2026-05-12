package com.vidv13.dashcode.ui.detail

import android.app.Activity
import android.graphics.Bitmap
import android.view.WindowManager
import androidx.activity.ComponentActivity
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.ambient.AmbientLifecycleObserver
import androidx.wear.compose.material3.CircularProgressIndicator
import androidx.wear.compose.material3.Text

@Composable
fun QrCodeDetailScreen(
    modifier: Modifier = Modifier,
    viewModel: QrCodeDetailViewModel = hiltViewModel(),
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val isAmbient = rememberIsAmbient()

    KeepScreenOn()

    if (isAmbient) {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.Black),
            contentAlignment = Alignment.Center,
        ) {
            if (state.bitmap != null) {
                Image(
                    bitmap = state.bitmap!!.asImageBitmap(),
                    contentDescription = state.name,
                    modifier = Modifier.size(160.dp),
                )
            }
        }
    } else {
        Box(
            modifier = modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.Center,
        ) {
            if (state.bitmap != null) {
                QrContent(name = state.name, bitmap = state.bitmap!!)
            } else {
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
private fun rememberIsAmbient(): Boolean {
    val activity = LocalContext.current as? Activity ?: return false
    var isAmbient by remember { mutableStateOf(false) }
    DisposableEffect(activity) {
        val callback = object : AmbientLifecycleObserver.AmbientLifecycleCallback {
            override fun onEnterAmbient(ambientDetails: AmbientLifecycleObserver.AmbientDetails) {
                isAmbient = true
            }
            override fun onExitAmbient() {
                isAmbient = false
            }
            override fun onUpdateAmbient() {}
        }
        val observer = AmbientLifecycleObserver(activity, callback)
        (activity as ComponentActivity).lifecycle.addObserver(observer)
        onDispose {
            activity.lifecycle.removeObserver(observer)
        }
    }
    return isAmbient
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
