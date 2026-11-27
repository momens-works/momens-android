package com.momens.android.core.designsystem.component.bottomsheet

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.SheetState
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.R
import com.momens.android.core.common.extension.addFocusCleaner
import com.momens.android.core.common.extension.dragToDismiss
import com.momens.android.core.designsystem.component.textbox.MomensTextBox
import com.momens.android.core.designsystem.effect.momensBottomSheetShadow
import com.momens.android.core.designsystem.theme.MomensTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MomensBottomSheet(
    onDismiss: () -> Unit,
    dismissEnabled: Boolean = true,
    modifier: Modifier = Modifier,
    sheetState: SheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true),
    containerColor: Color = MomensTheme.colors.white,
    dragHandle: @Composable (() -> Unit)? = { MomensDragHandle() },
    content: @Composable ColumnScope.() -> Unit,
) {
    val shape = RoundedCornerShape(
        topStart = 20.dp,
        topEnd = 20.dp,
    )

    ModalBottomSheet(
        onDismissRequest = { if (dismissEnabled) onDismiss() },
        sheetState = sheetState,
        shape = RectangleShape,
        containerColor = Color.Transparent,
        dragHandle = null,
        sheetGesturesEnabled = false,
        contentWindowInsets = { WindowInsets(0.dp) },
    ) {
        MomensBottomSheetContent(
            shape = shape,
            sheetState = sheetState,
            onDismiss = onDismiss,
            dismissEnabled = dismissEnabled,
            modifier = modifier,
            containerColor = containerColor,
            dragHandle = dragHandle,
            content = content,
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MomensBottomSheetContent(
    shape: RoundedCornerShape,
    sheetState: SheetState,
    onDismiss: () -> Unit,
    dismissEnabled: Boolean,
    containerColor: Color,
    modifier: Modifier = Modifier,
    dragHandle: @Composable (() -> Unit)?,
    content: @Composable ColumnScope.() -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = modifier
            .dragToDismiss(sheetState = sheetState, onDismiss = onDismiss, dismissEnabled = dismissEnabled)
            .fillMaxWidth()
            .padding(top = 25.dp)
            .momensBottomSheetShadow(shape = shape)
            .background(color = containerColor, shape = shape)
            .navigationBarsPadding()
            .addFocusCleaner(focusManager),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        dragHandle?.invoke()
        content()
    }
}

@Composable
private fun MomensDragHandle(
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier
            .padding(vertical = 16.dp)
            .size(width = 73.dp, height = 3.dp)
            .background(
                color = MomensTheme.colors.gray100,
                shape = RoundedCornerShape(100.dp),
            ),
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun MomensBottomSheetPreview() {
    MomensTheme() {
        Box(
            modifier = Modifier.fillMaxSize(),
        ) {
            MomensBottomSheet(
                onDismiss = {},
            ) {
                Column(
                    modifier = Modifier
                        .padding(
                            horizontal = 20.dp,
                            vertical = 16.dp,
                        ),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Row(
                        modifier = Modifier
                            .align(Alignment.Start),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Text(
                            text = "PM",
                            color = MomensTheme.colors.gray400,
                            style = MomensTheme.typography.captionM11,
                        )

                        Box(
                            modifier = Modifier
                                .size(2.dp)
                                .clip(CircleShape)
                                .background(color = MomensTheme.colors.gray500),
                        )

                        Text(
                            text = "Copy policy",
                            color = MomensTheme.colors.gray400,
                            style = MomensTheme.typography.captionM11,
                        )
                    }
                    Text(
                        text = "회원가입 에러 메시지 정책 초안",
                        modifier = Modifier
                            .align(Alignment.Start),
                        color = MomensTheme.colors.gray900,
                        style = MomensTheme.typography.bodyB16,
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Text(
                        text = "회원가입의 MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                        modifier = Modifier
                            .align(Alignment.Start),
                        color = MomensTheme.colors.gray400,
                        style = MomensTheme.typography.bodyM12,
                    )
                    Text(
                        text = "회원가입의 MVP 완료율과 온보딩 품질에 영향을 줄 수 있습니다.",
                        modifier = Modifier
                            .align(Alignment.Start),
                        color = MomensTheme.colors.gray400,
                        style = MomensTheme.typography.bodyM12,
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    MomensTextBox(
                        text = "회원가입 에러 메시지 정책 초안",
                        modifier = Modifier,
                        iconResId = R.drawable.ic_file,
                        iconColor = MomensTheme.colors.primary50,
                        textColor = MomensTheme.colors.gray500,
                        isArrowVisible = true,
                    )
                }
            }
        }
    }
}
