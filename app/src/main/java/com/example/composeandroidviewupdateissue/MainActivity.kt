package com.example.composeandroidviewupdateissue

import android.os.Bundle
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.TextView
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import kotlinx.coroutines.delay

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val state = rememberLazyListState()
            LaunchedEffect(Unit) {
                // "Updated string!" shows
                delay(2000)
                // Scroll to make AndroidView invisible
                state.scrollToItem(1, 200)
                // Mock the user behavior that scrolls slowly
                while (state.canScrollBackward) {
                    state.animateScrollBy(-50f)
                    delay(50)
                }
                // Observed that "Updated string!" is not showing
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background),
                state = state,
            ) {
                item {
                    var message: String? by remember { mutableStateOf(null) }
                    var composeTrigger by remember { mutableStateOf(false) }
                    LaunchedEffect(Unit) {
                        delay(20)
                        message = "Updated string!"
                    }
                    AndroidView(
                        factory = {
                            FrameLayout(it).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    300
                                )
                            }
                        },
                        onReset = { it.removeAllViews() },
                        update = {
                            if (message == null || composeTrigger) {
                                return@AndroidView
                            }

                            it.addView(
                                TextView(it.context).apply {
                                    text = message
                                    layoutParams = FrameLayout.LayoutParams(
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        ViewGroup.LayoutParams.WRAP_CONTENT,
                                        Gravity.CENTER
                                    )
                                    setTextColor(it.context.getColor(R.color.black))
                                }
                            )
                            composeTrigger = true
                        }
                    )
                }

                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.primary)
                            .height(1000.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text ="Scrolling",
                            color = Color.White
                        )
                    }
                }

            }
        }
    }
}