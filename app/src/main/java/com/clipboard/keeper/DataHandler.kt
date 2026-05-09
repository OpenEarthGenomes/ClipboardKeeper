package com.clipboard.keeper

import android.content.Context
import org.json.JSONArray
import java.io.File

object DataHandler {
    private const val FILE_NAME = "clipboard_data.json"
    fun saveText(context: Context, text: String) {
        val file = File(context.filesDir, FILE_NAME)
        val json = if (file.exists()) JSONArray(file.readText()) else JSONArray()
        if (json.length() > 0 && json.getString(json.length() - 1) == text) return
        json.put(text)
        file.writeText(json.toString())
    }
    fun getHistory(context: Context): List<String> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()
        val json = JSONArray(file.readText())
        return List(json.length()) { json.getString(it) }
    }
}
