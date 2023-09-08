package com.bkcoding.contactsyncapp

import android.Manifest
import android.accounts.Account
import android.accounts.AccountManager
import android.app.Application
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Handler
import android.os.Looper
import android.provider.ContactsContract
import androidx.core.os.bundleOf
import com.bkcoding.contactsyncapp.utils.ContactHelper
import com.bkcoding.contactsyncapp.datastore.ContactDataStore
import com.bkcoding.contactsyncapp.observer.ContactContentObserver
import com.bkcoding.contactsyncapp.utils.Constant.ACCOUNT
import com.bkcoding.contactsyncapp.utils.Constant.ACCOUNT_TYPE
import com.bkcoding.contactsyncapp.utils.Constant.AUTHORITY
import com.bkcoding.contactsyncapp.utils.Constant.SYNC_INTERVAL
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class ContactApp : Application() {

    @Inject
    lateinit var contactManager: ContactHelper

    @Inject
    lateinit var contactDataStore: ContactDataStore

    override fun onCreate() {
        super.onCreate()
        /**
         * populate database
         */
        if (checkSelfPermission(Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {
            /**
             * Create sync account
             */
            createSyncAccount(context = this)

            /**
             * Create a content observer object to object change for the tables contacts
             */
            val observer = ContactContentObserver(
                handler = Handler(Looper.getMainLooper())
            )

            /**
             * Register the observer for the data table
             */
            contentResolver.registerContentObserver(
                ContactsContract.Contacts.CONTENT_URI,
                true,
                observer
            )

            /**
             * add periodic sync interval
             */
            ContentResolver.addPeriodicSync(
                Account(ACCOUNT, ACCOUNT_TYPE),
                AUTHORITY,
                bundleOf(),
                SYNC_INTERVAL
            )
        }
    }

    private fun createSyncAccount(context: Context): Account {
        val accountManager = AccountManager.get(context)
        return Account(ACCOUNT, ACCOUNT_TYPE).also { newAccount ->
            /*
             * Add the account and account type, no password or user data
             * If successful, return the Account object, otherwise report an error.
             */
            if (accountManager.addAccountExplicitly(newAccount, null, null)) {
                ContentResolver.setSyncAutomatically(newAccount, AUTHORITY, true)
            } else {
                /*
                 * The account exists or some other error occurred. Log this, report it,
                 * or handle it internally.
                 */
            }
        }
    }
}