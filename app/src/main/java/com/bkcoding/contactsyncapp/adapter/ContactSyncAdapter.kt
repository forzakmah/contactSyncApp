package com.bkcoding.contactsyncapp.adapter

import android.accounts.Account
import android.content.AbstractThreadedSyncAdapter
import android.content.ContentProviderClient
import android.content.Context
import android.content.SyncResult
import android.os.Bundle
import com.bkcoding.contactsyncapp.utils.ContactHelper
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ContactSyncAdapter @Inject constructor(
    @ApplicationContext context: Context
) : AbstractThreadedSyncAdapter(context, true, false) {

    @Inject
    lateinit var contactManager: ContactHelper

    override fun onPerformSync(
        account: Account?,
        bundle: Bundle?,
        string: String?,
        providerClient: ContentProviderClient?,
        syncResult: SyncResult?
    ) {
        contactManager.syncContacts()
    }
}