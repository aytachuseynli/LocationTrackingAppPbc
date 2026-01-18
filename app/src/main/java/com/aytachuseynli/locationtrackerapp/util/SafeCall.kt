package com.aytachuseynli.locationtrackerapp.util

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend inline fun <T> safeCall(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T
): Result<T> {
    return withContext(dispatcher) {
        try {
            Result.success(block())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}

suspend inline fun <T> safeCallWithMapping(
    dispatcher: CoroutineDispatcher = Dispatchers.IO,
    crossinline block: suspend () -> T?,
    crossinline errorMessage: () -> String = { "Operation failed" }
): Result<T> {
    return withContext(dispatcher) {
        try {
            block()?.let {
                Result.success(it)
            } ?: Result.failure(Exception(errorMessage()))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
