package com.bkcoding.contactsyncapp.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import javax.inject.Inject

const val STORE_NAME = "app:datastore-contact-sync-app"
private val Context.dataStore: DataStore<androidx.datastore.preferences.core.Preferences> by preferencesDataStore(
    STORE_NAME
)

class ContactDataStore @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        private val isContactSynchronizedKey = booleanPreferencesKey("is_contact_synchronized_key")
    }


    val isContactSynchronized: Boolean
        get() = runBlocking {
            context.dataStore.data.map { it[isContactSynchronizedKey] }.first() ?: false
        }

    suspend fun setContactSynchronized(newState: Boolean) {
        context.dataStore.edit {
            it[isContactSynchronizedKey] = newState
        }
    }

}