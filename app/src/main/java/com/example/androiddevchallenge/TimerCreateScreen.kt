package com.example.androiddevchallenge

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Backspace
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.androiddevchallenge.ui.theme.JetbrainsFontFamily

@ExperimentalFoundationApi
@Composable
fun TimerCreateScreen(
    state: TimerStateManipulator
) {
    ConstraintLayout(Modifier.fillMaxSize()) {
        val (clock, numpad, button) = createRefs()

        CurrentValue(
            modifier = Modifier.constrainAs(clock) {
                centerHorizontallyTo(parent)
                top.linkTo(parent.top, margin = 24.dp)
            },
            digits = state.state.value.digits
        )

        NumPad(
            modifier = Modifier.constrainAs(numpad) {
                linkTo(top = clock.bottom, bottom = button.top)
            },
            onDigitClicked = { state.addDigit(it) },
            onBackspaceClicked = { state.removeDigit() },
            onBackspaceLongClicked = { state.removeAllDigits() }
        )

        FloatingActionButton(
            modifier = Modifier.constrainAs(button) {
                centerHorizontallyTo(parent)
                bottom.linkTo(parent.bottom, margin = 24.dp)
            },
            onClick = { state.startTimer() }
        ) {
            Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "Start timer")
        }
    }
}

@ExperimentalFoundationApi
@Composable
fun NumPadButton(
    onClick: () -> Unit,
    onLongClick: (() -> Unit)? = null,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable () -> Unit,
) {
    val clickMod = Modifier.combinedClickable(
        onClick = onClick,
        onLongClick = onLongClick,
        role = Role.Button,
        interactionSource = interactionSource,
        indication = rememberRipple(radius = 64.dp, bounded = false)
    )
    Box(
        modifier = Modifier
            .heightIn(max = 72.dp)
            .aspectRatio(1F),
        contentAlignment = Alignment.Center
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .then(clickMod),
            contentAlignment = Alignment.Center
        ) { content() }
    }
}

@ExperimentalFoundationApi
@Composable
fun NumPad(
    modifier: Modifier,
    onDigitClicked: (num: Int) -> Unit,
    onBackspaceClicked: () -> Unit,
    onBackspaceLongClicked: () -> Unit,
) {
    LazyVerticalGrid(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 32.dp),
        cells = GridCells.Fixed(3),
        contentPadding = PaddingValues(horizontal = 0.dp, vertical = 32.dp)
    ) {
        for (i in 1..9) {
            item {
                NumPadButton(
                    onClick = { onDigitClicked(i) },
                    content = { DigitButtonContent(i) },
                )
            }
        }
        item {  }
        item {
            NumPadButton(
                onClick = { onDigitClicked(0) },
                content = { DigitButtonContent(0) },
            )
        }
        item {
            NumPadButton(
                onClick = onBackspaceClicked,
                onLongClick = onBackspaceLongClicked,
            ) {
                Icon(
                    imageVector = Icons.Default.Backspace,
                    contentDescription = "Backspace",
                )
            }
        }
    }
}

@Composable
fun DigitButtonContent(digit: Int) {
    Text(
        text = "$digit",
        fontFamily = JetbrainsFontFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 32.sp,
    )
}

@Composable
fun CurrentValue(
    modifier: Modifier,
    digits: String
) {
    val text = digits
        .padStart(6, '0')
        .chunked(2)
        .joinToString(separator = ":")

    Text(
        modifier = modifier,
        text = text,
        fontFamily = JetbrainsFontFamily,
        fontSize = 48.sp,
        fontWeight = FontWeight.Medium,
    )
}