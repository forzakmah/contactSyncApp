package com.bkcoding.contactsyncapp.utils

object Constant {
    // The authority for the sync adapter's content provider
    const val AUTHORITY = "com.bkcoding.contactsyncapp.provider"

    // An account type, in the form of a domain name
    const val ACCOUNT_TYPE = "com.bkcoding.contactsyncapp.account"

    // The account name
    const val ACCOUNT = "ContactSyncApp"

    const val SECONDS_PER_MINUTE = 60L
    const val SYNC_INTERVAL_IN_MINUTES = 15L
    const val SYNC_INTERVAL = SYNC_INTERVAL_IN_MINUTES * SECONDS_PER_MINUTE
}