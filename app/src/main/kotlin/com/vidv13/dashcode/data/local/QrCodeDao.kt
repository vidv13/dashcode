package com.vidv13.dashcode.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface QrCodeDao {
    @Query("SELECT * FROM qr_codes ORDER BY sortOrder ASC")
    fun getAll(): Flow<List<QrCode>>

    @Query("SELECT * FROM qr_codes WHERE id = :id")
    suspend fun getById(id: Long): QrCode?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(codes: List<QrCode>)

    @Query("DELETE FROM qr_codes")
    suspend fun deleteAll()
}
