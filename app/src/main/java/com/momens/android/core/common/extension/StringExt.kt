package com.momens.android.core.common.extension

import java.time.Duration
import java.time.OffsetDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

fun String?.toRelativeTimeText(): String {
    if (this.isNullOrBlank()) return "-"
    return runCatching {
        val occurredAt = OffsetDateTime.parse(this)
        val minutes = Duration.between(occurredAt, OffsetDateTime.now()).toMinutes()
        when {
            minutes < 1 -> "방금 전"
            minutes < 60 -> "${minutes}분 전"
            minutes < 60 * 24 -> "${minutes / 60}시간 전"
            else -> "${minutes / (60 * 24)}일 전"
        }
    }.getOrDefault("-")
}

fun String?.toKstDateTimeText(): String {
    if (this.isNullOrBlank()) return "-"
    return runCatching {
        OffsetDateTime.parse(this)
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
    }.getOrDefault("-")
}

fun String.limitLength(maxLength: Int): String = if (length > maxLength) take(maxLength) else this

fun String.ellipsize(maxLength: Int): String =
    if (length > maxLength) take(maxLength - 1) + "…" else this
