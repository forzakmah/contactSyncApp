package com.bkcoding.contactsyncapp.observer

import android.accounts.Account
import android.content.ContentResolver
import android.content.SyncRequest
import android.database.ContentObserver
import android.net.Uri
import android.os.Handler
import androidx.core.os.bundleOf
import com.bkcoding.contactsyncapp.utils.Constant.ACCOUNT
import com.bkcoding.contactsyncapp.utils.Constant.ACCOUNT_TYPE
import com.bkcoding.contactsyncapp.utils.Constant.AUTHORITY

class ContactContentObserver(
    handler: Handler
) : ContentObserver(handler) {
    override fun onChange(selfChange: Boolean) {
        onChange(selfChange, null)
    }

    override fun onChange(selfChange: Boolean, uri: Uri?) {
        val account = Account(ACCOUNT, ACCOUNT_TYPE)
        val syncRequest = SyncRequest.Builder()
            /**
             * the bundle is required i verified the java implementation of requestSync function
             */
            .setExtras(bundleOf())
            .setSyncAdapter(account, AUTHORITY)
            .build()
        ContentResolver.requestSync(syncRequest)
    }
}