package com.vidv13.dashcode.di

import android.content.Context
import androidx.room.Room
import com.vidv13.dashcode.data.QrCodeRepository
import com.vidv13.dashcode.data.QrCodeRepositoryImpl
import com.vidv13.dashcode.data.local.QrCodeDao
import com.vidv13.dashcode.data.local.QrCodeDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    abstract fun bindRepository(impl: QrCodeRepositoryImpl): QrCodeRepository

    companion object {
        @Provides
        @Singleton
        fun provideDatabase(@ApplicationContext context: Context): QrCodeDatabase =
            Room.databaseBuilder(context, QrCodeDatabase::class.java, "dashcode.db").build()

        @Provides
        fun provideDao(db: QrCodeDatabase): QrCodeDao = db.qrCodeDao()
    }
}
