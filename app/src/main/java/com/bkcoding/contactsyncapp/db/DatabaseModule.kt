package com.bkcoding.contactsyncapp.db

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun providesContactDatabase(context: Context): ContactDatabase {
        return Room.databaseBuilder(
            context = context,
            klass = ContactDatabase::class.java,
            name = "contact_sync_app_database"
        ).build()
    }
}