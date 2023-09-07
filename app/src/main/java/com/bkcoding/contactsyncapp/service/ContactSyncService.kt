package com.bkcoding.contactsyncapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bkcoding.contactsyncapp.adapter.ContactSyncAdapter
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class ContactSyncService : Service() {

    @Inject
    lateinit var contactSyncAdapter: ContactSyncAdapter

    override fun onBind(p0: Intent?): IBinder? {
        return contactSyncAdapter.syncAdapterBinder
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId)
        return START_STICKY
    }
}