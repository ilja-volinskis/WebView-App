package com.example.webviewapp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

private sealed class Screen {
    data object LinkViewChoice : Screen()
    data class EmbeddedWebView(val url: String) : Screen()
}

@Composable
fun MainScreen() {
    var screen by remember { mutableStateOf<Screen>(Screen.LinkViewChoice) }

    when (val current = screen) {
        is Screen.LinkViewChoice -> LinkViewScreen(
            onOpenWebView = { screen = Screen.EmbeddedWebView(it) }
        )
        is Screen.EmbeddedWebView -> EmbeddedWebViewScreen(
            url = current.url,
            onBack = { screen = Screen.LinkViewChoice }
        )
    }
}