package com.clipboard.keeper

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
import com.clipboard.keeper.databinding.ActivityMainBinding
import com.clipboard.keeper.ui.BottomSheetMenu
import com.clipboard.keeper.ui.HistoryAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var adapter: HistoryAdapter
    private lateinit var db: ClipboardDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        db = ClipboardDatabase.getDatabase(this)

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

        // Adatok automatikus betöltése és frissítése
        lifecycleScope.launch {
            db.dao().getAllPreviews().collectLatest { previews ->
                adapter.submitList(previews)
            }
        }

        // Összes törlése gomb logikája
        binding.btnClear.setOnClickListener {
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

