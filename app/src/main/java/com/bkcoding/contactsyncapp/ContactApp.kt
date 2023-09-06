package com.bkcoding.contactsyncapp

import android.app.Application
import com.bkcoding.contactsyncapp.adapter.ContactManager
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ContactApp : Application() {

    @Inject
    lateinit var contactManager: ContactManager
    override fun onCreate() {
        super.onCreate()
        contactManager.populateDatabase()
    }
}