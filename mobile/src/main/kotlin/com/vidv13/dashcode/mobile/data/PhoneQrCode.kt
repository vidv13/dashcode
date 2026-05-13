package com.vidv13.dashcode.mobile.data

import kotlinx.serialization.Serializable

@Serializable
data class PhoneQrCode(
    val name: String,
    val content: String,
    val sortOrder: Int = 0,
)
