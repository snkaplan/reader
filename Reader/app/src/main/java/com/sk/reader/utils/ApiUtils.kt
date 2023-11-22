package com.sk.reader.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resumeWithException

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): ApiResult<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            ApiResult.Success(body)
        } else {
            ApiResult.Error(message = response.message())
        }
    } catch (e: HttpException) {
        ApiResult.Error(message = e.message())
    } catch (e: Throwable) {
        ApiResult.Error(message = e.message ?: "General Exception")
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
suspend fun <T> Task<T>.await(): T {
    return suspendCancellableCoroutine { cont ->
        addOnCompleteListener {
            if (it.exception != null) {
                cont.resumeWithException(it.exception!!)
            } else {
                cont.resume(it.result, null)
            }
        }
    }
}

sealed class ApiResult<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : ApiResult<T>(data)
    class Error<T>(data: T? = null, message: String) : ApiResult<T>(data, message)
}
