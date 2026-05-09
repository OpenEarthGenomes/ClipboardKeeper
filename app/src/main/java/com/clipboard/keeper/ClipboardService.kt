// app/src/main/java/com/clipboard/keeper/ClipboardService.kt
package com.clipboard.keeper

import android.app.Service
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.IBinder

class ClipboardService : Service() {

    private var clipboardManager: ClipboardManager? = null

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        
        // Figyelő indítása
        clipboardManager?.addPrimaryClipChangedListener {
            val clipData = clipboardManager?.primaryClip
            if (clipData != null && clipData.itemCount > 0) {
                val text = clipData.getItemAt(0).text?.toString()
                if (!text.isNullOrEmpty()) {
                    // Mentés a JSON-be a DataHandler segítségével
                    DataHandler.saveText(this, text)
                }
            }
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY // Ha az Android kilőné, induljon újra magától
    }

    override fun onBind(intent: Intent?): IBinder? = null
}
