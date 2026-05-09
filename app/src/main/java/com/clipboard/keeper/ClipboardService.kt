package com.clipboard.keeper

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.clipboard.keeper.data.ClipboardDatabase
import com.clipboard.keeper.data.ClipboardEntry
import kotlinx.coroutines.*

class ClipboardService : Service() {

    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private lateinit var clipboardManager: android.content.ClipboardManager

    override fun onCreate() {
        super.onCreate()
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
        startForegroundService()
        listenToClipboard()
    }

    private fun listenToClipboard() {
        clipboardManager.addPrimaryClipChangedListener {
            val clip = clipboardManager.primaryClip
            val text = clip?.getItemAt(0)?.text?.toString()
            if (!text.isNullOrEmpty() && text.length <= 10485760) { // Max 10MB
                serviceScope.launch {
                    val db = ClipboardDatabase.getDatabase(applicationContext)
                    db.dao().insert(ClipboardEntry(content = text))
                }
            }
        }
    }

    private fun startForegroundService() {
        val channelId = "clipboard_channel"
        val channelName = "Vágólap Figyelő"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_LOW)
            val manager = getSystemService(NotificationManager::class.java)
            manager?.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(this, channelId)
            .setContentTitle("Clipboard Keeper")
            .setContentText("Vágólap figyelés aktív...")
            .setSmallIcon(android.R.drawable.ic_menu_save)
            .setPriority(NotificationCompat.PRIORITY_LOW)
            .setOngoing(true)
            .build()

        startForeground(1, notification)
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}

