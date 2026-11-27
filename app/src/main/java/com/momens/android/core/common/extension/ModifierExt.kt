package com.momens.android.core.common.extension

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.findRootCoordinates
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInWindow
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.SoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

@Composable
inline fun Modifier.noRippleClickable(
    enabled: Boolean = true,
    crossinline onClick: () -> Unit,
): Modifier {
    return clickable(
        indication = null,
        interactionSource = remember { MutableInteractionSource() },
        onClick = { onClick() },
        enabled = enabled,
    )
}

@Composable
inline fun Modifier.noRippleToggleable(
    value: Boolean,
    enabled: Boolean = true,
    role: Role? = null,
    crossinline onValueChange: (Boolean) -> Unit,
): Modifier {
    return toggleable(
        value = value,
        interactionSource = remember { MutableInteractionSource() },
        indication = null,
        enabled = enabled,
        role = role,
        onValueChange = { onValueChange(it) },
    )
}

fun Modifier.addFocusCleaner(focusManager: FocusManager): Modifier {
    return this.pointerInput(focusManager) {
        detectTapGestures(
            onTap = { focusManager.clearFocus() },
        )
    }
}

fun Modifier.hideKeyboardOnFocusLost(
    keyboardController: SoftwareKeyboardController?,
): Modifier {
    return this.onFocusChanged { focusState ->
        if (!focusState.isFocused) {
            keyboardController?.hide()
        }
    }
}

@Composable
fun Modifier.advancedImePadding(): Modifier {
    var consumePadding by remember { mutableStateOf(0) }
    val density = LocalDensity.current

    return onGloballyPositioned { coordinates ->
        consumePadding = (
            coordinates.findRootCoordinates().size.height -
                (coordinates.positionInWindow().y + coordinates.size.height).toInt()
            ).coerceAtLeast(
                0,
            )
    }
        .consumeWindowInsets(
            PaddingValues(bottom = with(density) { consumePadding.toDp() }),
        )
        .imePadding()
}

@OptIn(ExperimentalMaterial3Api::class)
fun Modifier.dragToDismiss(
    sheetState: SheetState,
    onDismiss: () -> Unit,
    dismissEnabled: Boolean = true,
    positionalThreshold: Dp = 56.dp,
    velocityThreshold: Dp = 125.dp,
): Modifier = composed {
    val density = LocalDensity.current
    var dragOffsetPx by remember { mutableFloatStateOf(0f) }
    val settleOffset = remember { Animatable(0f) }

    LaunchedEffect(dismissEnabled) {
        if (!dismissEnabled) {
            dragOffsetPx = 0f
            settleOffset.snapTo(0f)
        }
    }

    this
        .offset {
            val y = if (settleOffset.isRunning) settleOffset.value else dragOffsetPx
            IntOffset(x = 0, y = y.roundToInt())
        }
        .draggable(
            enabled = dismissEnabled,
            state = rememberDraggableState { delta ->
                dragOffsetPx = (dragOffsetPx + delta).coerceAtLeast(0f)
            },
            orientation = Orientation.Vertical,
            onDragStopped = { velocity ->
                val positionalThresholdPx = with(density) { positionalThreshold.toPx() }
                val velocityThresholdPx = with(density) { velocityThreshold.toPx() }

                if (dismissEnabled && (dragOffsetPx > positionalThresholdPx || velocity > velocityThresholdPx)) {
                    try {
                        sheetState.hide()
                    } finally {
                        onDismiss()
                    }
                } else {
                    settleOffset.snapTo(dragOffsetPx)
                    settleOffset.animateTo(
                        targetValue = 0f,
                        animationSpec = tween(durationMillis = 250),
                    )
                    dragOffsetPx = 0f
                }
            },
        )
}
