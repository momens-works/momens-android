package com.momens.android.core.designsystem.component.importantstatus

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.momens.android.core.designsystem.component.type.ImportantLevel
import com.momens.android.core.designsystem.component.type.ImportantTone
import com.momens.android.core.designsystem.theme.MomensTheme

@Composable
fun MomensImportantStatus(
    level: ImportantLevel,
    tone: ImportantTone,
    modifier: Modifier = Modifier,
) {
    val iconRes = when (tone) {
        ImportantTone.GRAY -> level.grayIcon
        ImportantTone.BLUE -> level.blueIcon
        ImportantTone.WHITE -> level.grayIcon
    }

    Row(
        modifier = modifier
            .background(
                color = tone.background(),
                shape = RoundedCornerShape(4.dp),
            )
            .padding(horizontal = 10.dp, vertical = 2.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp),
    ) {
        Image(
            painter = painterResource(id = iconRes),
            contentDescription = null,
        )

        Text(
            text = level.text,
            color = tone.textColor(),
            style = tone.textStyle(),
        )
    }
}


@Preview(showBackground = true)
@Composable
private fun MomensImportantStatusPreview() {
    MomensTheme {
        Column(
            modifier = Modifier.padding(10.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            MomensImportantStatus(level = ImportantLevel.LOW, tone = ImportantTone.GRAY)
            MomensImportantStatus(level = ImportantLevel.MEDIUM, tone = ImportantTone.GRAY)
            MomensImportantStatus(level = ImportantLevel.HIGH, tone = ImportantTone.GRAY)
            MomensImportantStatus(level = ImportantLevel.URGENT, tone = ImportantTone.GRAY)
            MomensImportantStatus(level = ImportantLevel.LOW, tone = ImportantTone.BLUE)
            MomensImportantStatus(level = ImportantLevel.MEDIUM, tone = ImportantTone.BLUE)
            MomensImportantStatus(level = ImportantLevel.HIGH, tone = ImportantTone.BLUE)
            MomensImportantStatus(level = ImportantLevel.URGENT, tone = ImportantTone.BLUE)
            MomensImportantStatus(level = ImportantLevel.LOW, tone = ImportantTone.WHITE)
            MomensImportantStatus(level = ImportantLevel.MEDIUM, tone = ImportantTone.WHITE)
            MomensImportantStatus(level = ImportantLevel.HIGH, tone = ImportantTone.WHITE)
            MomensImportantStatus(level = ImportantLevel.URGENT, tone = ImportantTone.WHITE)
        }
    }
}


