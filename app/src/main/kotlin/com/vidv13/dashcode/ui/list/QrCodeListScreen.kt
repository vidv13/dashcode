package com.vidv13.dashcode.ui.list

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.foundation.lazy.ScalingLazyColumn
import androidx.wear.compose.foundation.lazy.items
import androidx.wear.compose.material3.Button
import androidx.wear.compose.material3.ListHeader
import androidx.wear.compose.material3.Text
import com.vidv13.dashcode.data.local.QrCode

@Composable
fun QrCodeListScreen(
    onCodeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: QrCodeListViewModel = hiltViewModel(),
) {
    val codes by viewModel.codes.collectAsStateWithLifecycle()

    if (codes.isEmpty()) {
        EmptyState(modifier)
    } else {
        CodeList(codes, onCodeClick, modifier)
    }
}

@Composable
private fun CodeList(
    codes: List<QrCode>,
    onCodeClick: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    ScalingLazyColumn(modifier = modifier.fillMaxSize()) {
        item { ListHeader { Text("DashCode") } }
        items(codes, key = { it.id }) { code ->
            Button(
                onClick = { onCodeClick(code.id) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
            ) {
                Text(
                    text = code.name,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun EmptyState(modifier: Modifier = Modifier) {
    ScalingLazyColumn(modifier = modifier.fillMaxSize()) {
        item { ListHeader { Text("DashCode") } }
        item {
            Text(
                text = "Add codes via the companion app on your phone",
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp),
            )
        }
    }
}
