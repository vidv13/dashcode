@file:OptIn(ExperimentalMaterial3Api::class)

package com.vidv13.dashcode.mobile.ui

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DragHandle
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.vidv13.dashcode.mobile.data.PhoneQrCode
import com.vidv13.dashcode.mobile.util.detectCodeType
import kotlin.math.roundToInt

@Composable
fun QrCodeListScreen(
    modifier: Modifier = Modifier,
    viewModel: QrCodeViewModel = hiltViewModel(),
) {
    val codes by viewModel.codes.collectAsStateWithLifecycle()
    var showAddDialog by remember { mutableStateOf(false) }
    var codeToDelete by remember { mutableStateOf<PhoneQrCode?>(null) }
    val listState = rememberLazyListState()
    var draggedIndex by remember { mutableStateOf<Int?>(null) }
    var dragOffsetY by remember { mutableFloatStateOf(0f) }

    Scaffold(
        modifier = modifier,
        topBar = { TopAppBar(title = { Text("DashCode") }) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showAddDialog = true }) {
                Icon(Icons.Default.Add, contentDescription = "Add QR code")
            }
        },
    ) { padding ->
        if (codes.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
                contentAlignment = Alignment.Center,
            ) {
                Text("Tap + to add your first QR code")
            }
        } else {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding),
            ) {
                itemsIndexed(codes, key = { _, code -> "${code.name}|${code.content}" }) { index, code ->
                    val isDragged = draggedIndex == index
                    CodeItem(
                        code = code,
                        modifier = Modifier
                            .zIndex(if (isDragged) 1f else 0f)
                            .offset { IntOffset(0, if (isDragged) dragOffsetY.roundToInt() else 0) },
                        onDragStarted = {
                            draggedIndex = index
                            dragOffsetY = 0f
                        },
                        onDrag = { delta -> dragOffsetY += delta },
                        onDragEnded = {
                            val from = draggedIndex
                            if (from != null && codes.isNotEmpty()) {
                                val visibleItems = listState.layoutInfo.visibleItemsInfo
                                val itemHeight = visibleItems.find { it.index == from }?.size?.toFloat()
                                    ?: visibleItems.map { it.size }.average().toFloat().takeIf { it > 0.0 }
                                    ?: 160f
                                val to = (from + (dragOffsetY / itemHeight).roundToInt())
                                    .coerceIn(0, codes.lastIndex)
                                if (from != to) viewModel.reorderCodes(from, to)
                            }
                            draggedIndex = null
                            dragOffsetY = 0f
                        },
                        onLongClick = { codeToDelete = code },
                    )
                    HorizontalDivider()
                }
            }
        }
    }

    if (showAddDialog) {
        AddCodeDialog(
            onDismiss = { showAddDialog = false },
            onConfirm = { name, content ->
                viewModel.addCode(name, content)
                showAddDialog = false
            },
        )
    }

    codeToDelete?.let { code ->
        DeleteConfirmDialog(
            name = code.name,
            onDismiss = { codeToDelete = null },
            onConfirm = {
                viewModel.deleteCode(code)
                codeToDelete = null
            },
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun CodeItem(
    code: PhoneQrCode,
    modifier: Modifier = Modifier,
    onDragStarted: () -> Unit,
    onDrag: (Float) -> Unit,
    onDragEnded: () -> Unit,
    onLongClick: () -> Unit,
) {
    val codeType = detectCodeType(code.content)
    val currentOnDragStarted by rememberUpdatedState(onDragStarted)
    val currentOnDrag by rememberUpdatedState(onDrag)
    val currentOnDragEnded by rememberUpdatedState(onDragEnded)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .combinedClickable(onLongClick = onLongClick, onClick = {})
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Icon(
            imageVector = Icons.Default.DragHandle,
            contentDescription = "Drag to reorder",
            modifier = Modifier
                .padding(end = 8.dp)
                .pointerInput(Unit) {
                    detectDragGesturesAfterLongPress(
                        onDragStart = { currentOnDragStarted() },
                        onDrag = { change, dragAmount ->
                            change.consume()
                            currentOnDrag(dragAmount.y)
                        },
                        onDragEnd = { currentOnDragEnded() },
                        onDragCancel = { currentOnDragEnded() },
                    )
                },
        )
        Column(modifier = Modifier.weight(1f)) {
            Text(text = code.name)
            Text(text = code.content, maxLines = 1)
        }
        SuggestionChip(
            onClick = {},
            label = { Text(codeType.label) },
        )
    }
}

@Composable
private fun AddCodeDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, content: String) -> Unit,
) {
    var name by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add QR code") },
        text = {
            Column {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Name (e.g. Gym card)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                )
                Spacer(Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("QR content (text to encode)") },
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        },
        confirmButton = {
            TextButton(
                onClick = { if (name.isNotBlank() && content.isNotBlank()) onConfirm(name, content) },
            ) { Text("Add") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}

@Composable
private fun DeleteConfirmDialog(
    name: String,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete \"$name\"?") },
        text = { Text("This will remove it from your watch too.") },
        confirmButton = {
            TextButton(onClick = onConfirm) { Text("Delete") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        },
    )
}
