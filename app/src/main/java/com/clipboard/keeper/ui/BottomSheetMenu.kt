package com.clipboard.keeper.ui

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.clipboard.keeper.R
import com.clipboard.keeper.data.ClipboardDatabase
import com.clipboard.keeper.data.ClipboardPreview
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class BottomSheetMenu(
    private val context: Context,
    private val entryId: Int,
    private val db: ClipboardDatabase,
    private val onUpdate: (ClipboardPreview) -> Unit
) {
    private lateinit var dialog: BottomSheetDialog
    
    fun show() {
        dialog = BottomSheetDialog(context)
        val view = context.layoutInflater.inflate(R.layout.bottom_sheet_menu, null)
        dialog.setContentView(view)
        
        view.findViewById<Button>(R.id.btnCopyToClipboard).setOnClickListener {
            copyToClipboard()
        }
        
        view.findViewById<Button>(R.id.btnDeleteEntry).setOnClickListener {
            deleteEntry()
        }
        
        view.findViewById<Button>(R.id.btnExportJSON).setOnClickListener {
            exportAsJson()
        }
        
        dialog.show()
    }
    
    private fun copyToClipboard() {
        CoroutineScope(Dispatchers.IO).launch {
            val fullText = db.dao().getFullContent(entryId)
            withContext(Dispatchers.Main) {
                val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                val clip = ClipData.newPlainText("copied", fullText)
                clipboard.setPrimaryClip(clip)
                Toast.makeText(context, "Teljes szöveg másolva a vágólapra", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            }
        }
    }
    
    private fun deleteEntry() {
        AlertDialog.Builder(context)
            .setTitle("Elem törlése")
            .setMessage("Biztosan törlöd ezt az előzményt?")
            .setPositiveButton("Igen") { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                    db.dao().deleteById(entryId)
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Elem törölve", Toast.LENGTH_SHORT).show()
                        dialog.dismiss()
                    }
                }
            }
            .setNegativeButton("Mégse", null)
            .show()
    }
    
    private fun exportAsJson() {
        CoroutineScope(Dispatchers.IO).launch {
            val fullText = db.dao().getFullContent(entryId)
            val entry = db.dao().getAllPreviews().collect { list ->
                // Nem kell itt semmi
            }
            val json = JSONObject().apply {
                put("id", entryId)
                put("content", fullText)
                put("timestamp", System.currentTimeMillis())
                put("export_date", SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date()))
            }
            
            val fileName = "clipboard_export_${entryId}_${System.currentTimeMillis()}.json"
            val file = File(context.getExternalFilesDir(null), fileName)
            file.writeText(json.toString(2))
            
            withContext(Dispatchers.Main) {
                Toast.makeText(context, "Exportálva: ${file.absolutePath}", Toast.LENGTH_LONG).show()
                dialog.dismiss()
            }
        }
    }
}
