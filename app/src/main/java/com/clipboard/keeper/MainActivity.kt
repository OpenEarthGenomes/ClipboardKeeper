package com.clipboard.keeper

import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.clipboard.keeper.data.ClipboardDatabase
import com.clipboard.keeper.data.ClipboardEntry
import com.clipboard.keeper.databinding.ActivityMainBinding
import com.clipboard.keeper.ui.BottomSheetMenu
import com.clipboard.keeper.ui.HistoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var db: ClipboardDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = ClipboardDatabase.getDatabase(this)

        // Értesítési engedély Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

        // Foreground Service indítása
        val serviceIntent = Intent(this, ClipboardService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            startForegroundService(serviceIntent)
        } else {
            startService(serviceIntent)
        }

        // RecyclerView beállítása
        adapter = HistoryAdapter { entryId ->
            val bottomSheet = BottomSheetMenu(this, entryId, db) { updatedEntry ->
                lifecycleScope.launch {
                    adapter.updateItem(updatedEntry)
                }
            }
            bottomSheet.show()
        }
        
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
        binding.recyclerView.adapter = adapter

        // Adatok betöltése
        lifecycleScope.launch {
            db.dao().getAllPreviews().collectLatest { previews ->
                adapter.submitList(previews)
            }
        }

        // Összes törlése gomb
        binding.btnClear.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cache törlése")
                .setMessage("Biztosan törlöd az összes vágólap előzményt?")
                .setPositiveButton("Igen") { _, _ ->
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            db.dao().clearAll()
                        }
                        Toast.makeText(this@MainActivity, "Összes előzmény törölve", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Mégse", null)
                .show()
        }
    }

    // Amint az app előtérbe kerül, kényszerítjük a vágólap olvasását
    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)
        if (hasFocus) {
            val clipboard = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            if (clipboard.hasPrimaryClip()) {
                val text = clipboard.primaryClip?.getItemAt(0)?.text?.toString()
                if (!text.isNullOrEmpty()) {
                    lifecycleScope.launch(Dispatchers.IO) {
                        db.dao().insert(ClipboardEntry(content = text))
                    }
                }
            }
        }
    }
}
