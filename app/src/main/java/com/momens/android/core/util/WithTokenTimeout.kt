package com.momens.android.core.util

import kotlinx.coroutines.withTimeoutOrNull
import java.net.SocketTimeoutException

suspend fun <T> withTokenTimeout(
    timeoutMillis: Long = 30_000L,
    block: suspend () -> T,
): T {
    val result = withTimeoutOrNull(timeoutMillis) {
        Result.success(block())
    } ?: throw SocketTimeoutException("Token request timed out after $timeoutMillis ms")

    return result.getOrThrow()
}
