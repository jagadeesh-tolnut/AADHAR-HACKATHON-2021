package com.example.gradient

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import com.example.gradient.ui.theme.GradientTheme
import com.example.gradient.ui.theme.lower
import com.example.gradient.ui.theme.upper

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Gradient()
        }
    }
}

@Composable
fun Gradient() {

    Box(modifier = Modifier
        .background(
            Brush.verticalGradient(
                colors = listOf(
                    upper,
                    lower
                ), startY = 400f
            )
        )
        .fillMaxSize()) {

    }
}

@Preview
@Composable
fun composablePreview()
{
    Gradient()
}