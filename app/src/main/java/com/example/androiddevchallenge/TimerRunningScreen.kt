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

import androidx.compose.animation.core.withInfiniteAnimationFrameMillis
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.JetbrainsFontFamily
import kotlinx.coroutines.isActive
import kotlin.math.absoluteValue
import kotlin.math.ceil

@Composable
fun TimerRunningScreen(
    screenState: TimerStateManipulator
) {
    val remainingMillis = remember { mutableStateOf(screenState.state.value.getRemainingMillis()) }
    LaunchedEffect(key1 = Unit) {
        while (isActive) {
            withInfiniteAnimationFrameMillis {
                remainingMillis.value = screenState.state.value.getRemainingMillis()
            }
        }
    }

    ConstraintLayout(Modifier.fillMaxSize()) {
        val (clock, bottomArea) = createRefs()

        Clock(
            modifier = Modifier.constrainAs(clock) {
                linkTo(parent.top, bottomArea.top)
                linkTo(parent.start, parent.end)
            },
            totalMillis = screenState.state.value.totalSeconds * 1000L,
            remainingMillis = remainingMillis.value
        )

        BottomArea(
            modifier = Modifier.constrainAs(bottomArea) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom, margin = 24.dp)
            },
            stateManipulator = screenState,
            remainingMillis = remainingMillis.value,
        )
    }
}

@Composable
fun Clock(
    modifier: Modifier,
    totalMillis: Long,
    remainingMillis: Long,
) {
    val totalSecs = ceil(remainingMillis.toFloat() / 1000F).toInt()
    val hours = totalSecs / 60 / 60
    val mins = totalSecs / 60 - hours * 60
    val secs = totalSecs - hours * 60 * 60 - mins * 60
    val text = buildString {
        if (hours != 0) {
            append(hours)
            append(":")
        }
        if (hours != 0 || mins != 0) {
            if (hours != 0) {
                append(mins.absoluteValue.toString().padStart(2, '0'))
            } else {
                append(mins)
            }
            append(":")
        }
        if (hours != 0 || mins != 0) {
            append(secs.absoluteValue.toString().padStart(2, '0'))
        } else {
            append(secs)
        }
    }
    val perc = if (remainingMillis <= 0) {
        0F
    } else {
        remainingMillis.toFloat() / totalMillis
    }
    val activeDeg = 360F * perc
    val inactiveDeg = 360F - activeDeg
    Box(
        modifier = modifier
            .padding(horizontal = 48.dp)
            .fillMaxWidth()
            .aspectRatio(1F),
    ) {
        val colorActive = MaterialTheme.colors.secondary
        val colorInactive = MaterialTheme.colors.secondaryVariant
        Canvas(modifier = Modifier.fillMaxSize()) {
            drawCircle(
                color = colorInactive,
                style = Stroke(width = 8.dp.toPx()),
            )
            drawArc(
                color = colorActive,
                startAngle = -90F,
                sweepAngle = -activeDeg,
                useCenter = false,
                style = Stroke(width = 8.dp.toPx(), cap = StrokeCap.Round)
            )
        }

        Text(
            modifier = Modifier.align(Alignment.Center),
            text = text,
            fontFamily = JetbrainsFontFamily,
            fontSize = 44.sp
        )
    }
}

fun DrawScope.drawClock(color: Color) {
    val deg = 360F / 24
    repeat(24) {
        val lineLen = when {
            it % 6 == 0 -> 16.dp
            it % 2 == 0 -> 12.dp
            else -> 8.dp
        }.toPx()

        val lineWidth = when {
            it % 6 == 0 -> 6.dp
            it % 2 == 0 -> 4.dp
            else -> 4.dp
        }.toPx()

        rotate(deg * it) {
            val yOffset = 0F
            drawLine(
                color,
                start = Offset(x = center.x, y = yOffset),
                end = Offset(x = center.x, y = lineLen + yOffset),
                strokeWidth = lineWidth,
                cap = StrokeCap.Round,
            )
        }
    }
}

@Composable
fun BottomArea(
    modifier: Modifier,
    stateManipulator: TimerStateManipulator,
    remainingMillis: Long,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val text = if (remainingMillis < 0) {
            "Time's up!"
        } else ""

        Text(text = text, fontFamily = JetbrainsFontFamily, fontSize = 24.sp)
        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = { stateManipulator.reset() }) {
            Text(text = "Clear", fontFamily = JetbrainsFontFamily)
        }
    }
}
