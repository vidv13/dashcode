package com.vidv13.dashcode.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity(tableName = "qr_codes")
data class QrCode(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val name: String,
    val content: String,
    val sortOrder: Int = 0,
)
