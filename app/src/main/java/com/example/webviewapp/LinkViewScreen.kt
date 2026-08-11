package com.example.webviewapp

import android.content.Intent
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri


private const val WEB_URL = "https://www.android.com"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LinkViewScreen(onOpenWebView: (String) -> Unit) {
    val context = LocalContext.current
    var showChoiceDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Welcome") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(24.dp)
                .fillMaxSize()
        ) {
            Text(
                text = "Please choose",
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = { showChoiceDialog = true },
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Text(
                    text = "Click to choose where to view the link",
                    fontSize = 18.sp
                )
            }
        }
    }

    if (showChoiceDialog) {
        LinkChoiceDialog(
            onDismiss = { showChoiceDialog = false },
            onOpenInBrowser = {
                showChoiceDialog = false
                val intent = Intent(Intent.ACTION_VIEW, WEB_URL.toUri())
                context.startActivity(intent)
            },
            onOpenInWebView = {
                showChoiceDialog = false
                onOpenWebView(WEB_URL)
            }
        )
    }
}

@Composable
private fun LinkChoiceDialog(
    onDismiss: () -> Unit,
    onOpenInBrowser: () -> Unit,
    onOpenInWebView: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Open Website") },
        text = { Text("Choose how you'd like to view this link.") },
        confirmButton = {
            TextButton(onClick = onOpenInBrowser) {
                Text("3rd-Party Browser")
            }
        },
        dismissButton = {
            TextButton(onClick = onOpenInWebView) {
                Text("Embedded WebView")
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmbeddedWebViewScreen(url: String, onBack: () -> Unit) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(WEB_URL) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        AndroidView(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize(),
            factory = { context ->
                WebView(context).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = WebViewClient()
                    settings.javaScriptEnabled = true
                    loadUrl(url)
                }
            }
        )
    }
}
