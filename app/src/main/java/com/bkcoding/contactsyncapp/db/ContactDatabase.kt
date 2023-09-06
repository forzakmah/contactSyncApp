package com.bkcoding.contactsyncapp.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.bkcoding.contactsyncapp.db.converter.DateConverter
import com.bkcoding.contactsyncapp.db.converter.ListConverter
import com.bkcoding.contactsyncapp.db.dao.ContactDao
import com.bkcoding.contactsyncapp.db.entity.ContactEntity

@Database(
    entities = [
        ContactEntity::class
    ],
    version = 1,
    autoMigrations = [],
    exportSchema = true
)
@TypeConverters(
    DateConverter::class,
    ListConverter::class
)
abstract class ContactDatabase : RoomDatabase() {
    abstract fun contactDao(): ContactDao
}