package com.vidv13.dashcode.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [QrCode::class], version = 2, exportSchema = false)
abstract class QrCodeDatabase : RoomDatabase() {
    abstract fun qrCodeDao(): QrCodeDao
}
