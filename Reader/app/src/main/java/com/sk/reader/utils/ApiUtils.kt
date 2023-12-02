package com.sk.reader.utils

import com.google.android.gms.tasks.Task
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import retrofit2.HttpException
import retrofit2.Response
import kotlin.coroutines.resumeWithException

suspend fun <T : Any> handleApi(
    execute: suspend () -> Response<T>
): Resource<T> {
    return try {
        val response = execute()
        val body = response.body()
        if (response.isSuccessful && body != null) {
            Resource.Success(body)
        } else {
            Resource.Error(message = response.message())
        }
    } catch (e: HttpException) {
        Resource.Error(message = e.message())
    } catch (e: Throwable) {
        Resource.Error(message = e.message ?: "General Exception")
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

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(data: T?) : Resource<T>(data)
    class Error<T>(data: T? = null, message: String) : Resource<T>(data, message)
}
