package com.clipboard.keeper

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.clipboard.keeper.data.ClipboardDatabase
import com.clipboard.keeper.ui.BottomSheetMenu
import com.clipboard.keeper.ui.HistoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var adapter: HistoryAdapter
    private lateinit var clearButton: Button
    private lateinit var db: ClipboardDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Értesítési engedély Android 13+ (API 33+)
        if (Build.VERSION.SDK_INT >= 33) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS)
                != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                    arrayOf(android.Manifest.permission.POST_NOTIFICATIONS), 100)
            }
        }

        // Foreground Service indítása
        startForegroundService(Intent(this, ClipboardService::class.java))

        db = ClipboardDatabase.getDatabase(this)

        // RecyclerView
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = HistoryAdapter { entryId ->
            val bottomSheet = BottomSheetMenu(this, entryId, db) { updatedEntry ->
                lifecycleScope.launch {
                    adapter.updateItem(updatedEntry)
                }
            }
            bottomSheet.show()
        }
        recyclerView.adapter = adapter

        // Adatok betöltése
        lifecycleScope.launch {
            db.dao().getAllPreviews().collectLatest { previews ->
                adapter.submitList(previews)
            }
        }

        // Összes törlése gomb
        clearButton = findViewById(R.id.btnClear)
        clearButton.setOnClickListener {
            AlertDialog.Builder(this)
                .setTitle("Cache törlése")
                .setMessage("Biztosan törlöd az összes vágólap előzményt?")
                .setPositiveButton("Igen") { _, _ ->
                    lifecycleScope.launch {
                        db.dao().clearAll()
                        Toast.makeText(this@MainActivity, "Összes előzmény törölve", Toast.LENGTH_SHORT).show()
                    }
                }
                .setNegativeButton("Mégse", null)
                .show()
        }
    }
}
