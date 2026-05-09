// app/src/main/java/com/clipboard/keeper/DataHandler.kt
package com.clipboard.keeper

import android.content.Context
import org.json.JSONArray
import java.io.File

object DataHandler {
    private const val FILE_NAME = "clipboard_data.json"

    // Mentés funkció
    fun saveText(context: Context, text: String) {
        val file = File(context.filesDir, FILE_NAME)
        val jsonArray = if (file.exists()) {
            JSONArray(file.readText())
        } else {
            JSONArray()
        }

        // Duplikáció ellenőrzés: ha az utolsó elem ugyanaz, nem mentjük el újra
        if (jsonArray.length() > 0 && jsonArray.getString(jsonArray.length() - 1) == text) {
            return
        }

        jsonArray.put(text)
        file.writeText(jsonArray.toString())
    }

    // Visszaolvasás a UI-nak
    fun getHistory(context: Context): List<String> {
        val file = File(context.filesDir, FILE_NAME)
        if (!file.exists()) return emptyList()

        return try {
            val jsonArray = JSONArray(file.readText())
            val list = mutableListOf<String>()
            for (i in 0 until jsonArray.length()) {
                list.add(jsonArray.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
