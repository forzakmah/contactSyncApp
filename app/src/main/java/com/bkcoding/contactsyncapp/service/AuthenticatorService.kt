package com.bkcoding.contactsyncapp.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.bkcoding.contactsyncapp.adapter.StubAuthenticator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AuthenticatorService : Service() {

    @Inject
    lateinit var authenticatorService: StubAuthenticator

    override fun onBind(intent: Intent?): IBinder = authenticatorService.iBinder
}