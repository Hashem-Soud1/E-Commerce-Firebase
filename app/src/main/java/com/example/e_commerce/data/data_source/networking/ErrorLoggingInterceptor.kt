package com.example.e_commerce.data.data_source.networking

import android.util.Log
import com.example.e_commerce.utils.CrashlyticsUtils
import com.google.android.gms.common.api.Response
import okhttp3.Interceptor
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

class ErrorLoggingInterceptor : Interceptor {
    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): okhttp3.Response {
        val originalResponse = chain.proceed(chain.request())

        if (!originalResponse.isSuccessful) {
            // Temporarily read the response body
            val responseBody = originalResponse.body
            val responseBodyString = responseBody?.string()

            // Log the error
            Log.e("API Error", "Request failed: $responseBodyString")
            CrashlyticsUtils.sendLogToCrashlytics(
                originalResponse.request.url.toString(),
                "Request failed: $responseBodyString"
            )

            // Re-create the response body before returning the response
            val newResponseBody = responseBodyString?.toResponseBody(responseBody.contentType())
            return originalResponse.newBuilder().body(newResponseBody).build()
        }

        return originalResponse
    }
}