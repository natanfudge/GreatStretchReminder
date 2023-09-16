package util

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import kotlin.coroutines.resume

private val okHttpJson = ("application/json; charset=utf-8").toMediaType()

suspend fun OkHttpClient.get(url: String): Response {
    val request = Request.Builder()
        .url(url)
        .get()
        .build()

    return executeSuspend(request)
}

private suspend fun OkHttpClient.executeSuspend(request: Request): Response {
    return suspendCancellableCoroutine { cont ->
        newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: okio.IOException) {
                cont.cancel(e)
            }

            override fun onResponse(call: Call, response: Response) {
                cont.resume(response)
            }
        })
    }
}