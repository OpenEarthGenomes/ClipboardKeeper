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
            // A neon stílus marad, közvetlenül a kódból!
            Surface(modifier = Modifier.fillMaxSize(), color = Color(0xFF050505)) {
                CyberUI()
            }
        }
    }

    @Composable
    fun CyberUI() {
        var history by remember { mutableStateOf(listOf<String>()) }
        LaunchedEffect(Unit) {
            history = DataHandler.getHistory(this@MainActivity)
        }

        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            Text(
                text = "> INNER_OS v1.0 // CLIPBOARD_MONITOR",
                color = Color(0xFF00FF41),
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "STATUS: ACTIVE_LINK",
                color = Color(0xFF00F3FF),
                fontSize = 10.sp,
                fontFamily = FontFamily.Monospace
            )
            Spacer(modifier = Modifier.height(20.dp))
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(history.reversed()) { item ->
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Brush.linearGradient(listOf(Color(0xFF00F3FF), Color(0xFFBC00FF))), RoundedCornerShape(8.dp))
                            .background(Color(0x0DFFFFFF), RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    ) {
                        Text(text = item, color = Color.White, fontSize = 15.sp)
                    }
                }
            }
        }
    }
}

