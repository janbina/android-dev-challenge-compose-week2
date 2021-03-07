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

import android.os.Parcelable
import android.os.SystemClock
import androidx.compose.runtime.MutableState
import com.example.androiddevchallenge.TimerState.Companion.MaxDigits
import kotlinx.parcelize.Parcelize

@Parcelize
data class TimerState(
    val digits: String = "",
    val started: Boolean = false,
    val startedAt: Long = 0L,
    val totalSeconds: Int = 0,
) : Parcelable {

    val canStart = !started && digits.toSeconds() > 0

    fun getRemainingMillis(): Long {
        if (startedAt == 0L) return 0
        val now = SystemClock.elapsedRealtime()
        val diff = now - startedAt
        return totalSeconds * 1000 - diff
    }

    companion object {
        const val MaxDigits = 6
    }
}

class TimerStateManipulator(
    val state: MutableState<TimerState>
) {

    fun addDigit(digit: Int) {
        val newValue = state.value.digits + "$digit"
        if (newValue.length <= MaxDigits) {
            state.value = state.value.copy(
                digits = newValue.trimStart { it == '0' }
            )
        }
    }

    fun removeDigit() {
        state.value = state.value.copy(
            digits = state.value.digits.dropLast(1)
        )
    }

    fun removeAllDigits() {
        state.value = state.value.copy(
            digits = ""
        )
    }

    fun startTimer() {
        val digits = state.value.digits
        val totalSeconds = digits.toSeconds()
        if (totalSeconds == 0) return
        state.value = state.value.copy(
            started = true,
            totalSeconds = totalSeconds,
            startedAt = SystemClock.elapsedRealtime(),
        )
    }

    fun reset() {
        state.value = TimerState()
    }
}

private fun String.toSeconds(): Int {
    val chunks = takeLast(MaxDigits).padStart(6, '0').chunked(2).map { it.toIntOrZero() }
    return chunks[0] * 60 * 60 + chunks[1] * 60 + chunks[2]
}

private fun String.toIntOrZero() = toIntOrNull() ?: 0
