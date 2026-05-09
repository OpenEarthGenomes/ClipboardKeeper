package com.clipboard.keeper.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.clipboard.keeper.ClipboardService

class BootReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {
            val serviceIntent = Intent(context, ClipboardService::class.java)
            context.startForegroundService(serviceIntent)
        }
    }
}
