package com.clipboard.keeper

import android.content.Context
import android.os.Environment
import java.io.File

class DataHandler(private val context: Context) {
    private val storageDir = context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS)
    private val jsonFile = File(storageDir, "clipboard_history.json")

    fun saveHistory(jsonString: String) {
        try {
            jsonFile.writeText(jsonString)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun loadHistory(): String {
        return if (jsonFile.exists()) jsonFile.readText() else ""
    }
}
