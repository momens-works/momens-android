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

/**
 * ISO 8601 UTC 문자열(예: "2026-07-15T13:48:47Z")을 KST 기준
 * "yyyy.MM.dd HH:mm" 형식으로 변환합니다.
 */
fun String?.toKstDateTimeText(): String {
    if (this.isNullOrBlank()) return "-"
    return runCatching {
        OffsetDateTime.parse(this)
            .atZoneSameInstant(ZoneId.of("Asia/Seoul"))
            .format(DateTimeFormatter.ofPattern("yyyy.MM.dd HH:mm"))
    }.getOrDefault("-")
}

/**
 * 공백을 포함한 글자 수 기준으로 [maxLength]를 초과하지 않도록 자릅니다.
 */
fun String.limitLength(maxLength: Int): String = if (length > maxLength) take(maxLength) else this

/**
 * 공백을 포함한 글자 수가 [maxLength]를 넘으면 앞 `maxLength - 1`자 뒤에 `…`를 붙여 총 [maxLength]자로 줄입니다.
 */
fun String.ellipsize(maxLength: Int): String =
    if (length > maxLength) take(maxLength - 1) + "…" else this
