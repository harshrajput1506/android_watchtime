package com.app.watchtime.core.error_handler

import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException


class ApiErrorHandler {

    fun handleError(throwable: Throwable): String {
        return when (throwable) {
            is retrofit2.HttpException -> {
                when (throwable.code()) {
                    401 -> "Unauthorized access"
                    403 -> "Access forbidden"
                    404 -> "Content not found"
                    in 500..599 -> "Server error occurred"
                    else -> "HTTP Error: ${throwable.code()}"
                }
            }
            is SocketTimeoutException ->  "Request timed out"
            is UnknownHostException, is IOException -> "No internet connection"
            else -> "Unknown error occurred"
        }
    }
}

