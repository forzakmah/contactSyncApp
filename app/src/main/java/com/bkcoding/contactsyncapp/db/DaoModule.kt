package com.bkcoding.contactsyncapp.db

import com.bkcoding.contactsyncapp.db.dao.ContactDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DaoModule {

    @Provides
    @Singleton
    fun providesContactDao(
        db: ContactDatabase
    ): ContactDao {
        return db.contactDao()
    }
}