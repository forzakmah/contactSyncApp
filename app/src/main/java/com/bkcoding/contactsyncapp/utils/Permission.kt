package com.bkcoding.contactsyncapp.utils

import android.content.Context
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat


fun hasPermissions(
    context: Context,
    permissions: List<String>
): Boolean {
    for (permission in permissions) {
        if (ActivityCompat.checkSelfPermission(context, permission) != PackageManager.PERMISSION_GRANTED) {
            return false
        }
    }
    return true
}

fun hasPermission(
    context: Context,
    permission: String
) = ContextCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED

@Composable
fun requestPermissionLauncher(
    permissionCallback: (Boolean) -> Unit
) = rememberLauncherForActivityResult(
    contract = ActivityResultContracts.RequestMultiplePermissions()
) { permissions ->
    val allPermissionGranted = permissions.values.reduce { acc, next -> acc && next }
    permissionCallback(allPermissionGranted)
}