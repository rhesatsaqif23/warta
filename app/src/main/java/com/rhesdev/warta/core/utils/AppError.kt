package com.rhesdev.warta.core.utils

import java.io.IOException
import retrofit2.HttpException

// User-facing error copy mapping for throwables surfacing in UiState.
object AppErrorText {
    const val NO_CONNECTION = "Periksa koneksi internet Anda"
    const val UNEXPECTED = "Terjadi kesalahan tak terduga"
}

fun Throwable.toUserMessage(): String = when (this) {
    is IOException -> AppErrorText.NO_CONNECTION
    is HttpException -> response()?.message()?.takeIf { it.isNotBlank() } ?: AppErrorText.UNEXPECTED
    else -> AppErrorText.UNEXPECTED
}