package com.vidv13.dashcode.util

sealed class CodeType(val label: String) {
    data object Url : CodeType("URL")
    data object Wifi : CodeType("WiFi")
    data object Text : CodeType("Text")
}

fun detectCodeType(content: String): CodeType = when {
    content.startsWith("http://") || content.startsWith("https://") -> CodeType.Url
    content.startsWith("WIFI:") -> CodeType.Wifi
    else -> CodeType.Text
}
