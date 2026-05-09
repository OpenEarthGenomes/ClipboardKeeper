package com.clipboard.keeper

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startService(Intent(this, ClipboardService::class.java))
        setContent {
            CyberUI()
        }
    }

    @Composable
    fun CyberUI() {
        var history by remember { mutableStateOf(listOf<String>()) }
        LaunchedEffect(Unit) {
            history = DataHandler.getHistory(this@MainActivity)
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xFF050505))
                .padding(16.dp)
        ) {
            Text(
                text = "> INNER_OS v1.0 // CLIPBOARD_MONITOR",
                color = Color(0xFF00FF41),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "STATUS: ACTIVE",
                color = Color(0xFF00F3FF),
                fontSize = 12.sp,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier.padding(bottom = 20.dp)
            )

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(history.reversed()) { item ->
                    DataCard(item)
                }
            }
        }
    }

    @Composable
    fun DataCard(text: String) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .border(
                    width = 1.dp,
                    brush = Brush.linearGradient(colors = listOf(Color(0xFF00F3FF), Color(0xFFBC00FF))),
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color(0x0DFFFFFF), RoundedCornerShape(8.dp))
                .padding(12.dp)
        ) {
            Column {
                Text(text = "RAW_DATA_HEX:", color = Color(0xFFBC00FF), fontSize = 9.sp, fontFamily = FontFamily.Monospace)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = text, color = Color.White, fontSize = 15.sp)
            }
        }
    }
}
