/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge

import android.os.Bundle
import android.view.View
import android.view.Window
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.androiddevchallenge.ui.theme.JetbrainsFontFamily
import com.example.androiddevchallenge.ui.theme.MyTheme

class MainActivity : AppCompatActivity() {
    @ExperimentalFoundationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyTheme {
                window.setStatusBarColor(MaterialTheme.colors.surface, isSystemInDarkTheme().not())
                MyApp()
            }
        }
    }
}

// Start building your app here!
@ExperimentalFoundationApi
@Composable
fun MyApp() {
    val screenState = rememberSaveable { mutableStateOf(TimerState()) }
    val screenManipulator = TimerStateManipulator(screenState)
    Surface(color = MaterialTheme.colors.background) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Toolbar()

            Crossfade(targetState = screenState.value.started) {
                if (it) {
                    TimerRunningScreen(screenState = screenManipulator)
                } else {
                    TimerCreateScreen(state = screenManipulator)
                }
            }
        }
    }
}

@Composable
fun Toolbar() {
    val isDark = isSystemInDarkTheme()
    Box(modifier = Modifier
        .fillMaxWidth()
        .height(56.dp)
        .padding(horizontal = 8.dp),
    ) {
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = "Timer",
            fontFamily = JetbrainsFontFamily,
            fontSize = 28.sp,
        )
        IconButton(
            modifier = Modifier.align(Alignment.CenterEnd),
            onClick = {
                AppCompatDelegate.setDefaultNightMode(
                    if (isDark) AppCompatDelegate.MODE_NIGHT_NO else AppCompatDelegate.MODE_NIGHT_YES
                )
            }
        ) {
            val icon = if (isDark) {
                Icons.Default.DarkMode
            } else {
                Icons.Default.LightMode
            }
            Icon(imageVector = icon, contentDescription = "Switch light/dark mode")
        }
    }
}

@Suppress("DEPRECATION")
fun Window.setStatusBarColor(
    color: Color,
    darkIcons: Boolean,
) {
    statusBarColor = color.toArgb()

    if (darkIcons) {
        decorView.systemUiVisibility = decorView.systemUiVisibility or
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
    } else {
        decorView.systemUiVisibility = decorView.systemUiVisibility and
                View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
    }
}