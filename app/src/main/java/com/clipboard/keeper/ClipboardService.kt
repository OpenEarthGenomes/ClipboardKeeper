package com.clipboard.keeper

import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder

class ClipboardService : Service() {
    override fun onCreate() {
        super.onCreate()
        val cb = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        cb.addPrimaryClipChangedListener {
            val text = cb.primaryClip?.getItemAt(0)?.text?.toString()
            if (!text.isNullOrEmpty()) DataHandler.saveText(this, text)
        }
    }
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int = START_STICKY
    override fun onBind(intent: Intent?): IBinder? = null
}
