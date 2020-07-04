package com.github.mooncoders.jammed.sdk.helpers

import okhttp3.Interceptor

internal class ApiKeyInterceptor(private val apiKey: String) : Interceptor {
    override fun intercept(chain: Interceptor.Chain) =
        chain.proceed(
            chain.request().newBuilder()
                .header("apikey", apiKey)
                .build()
        )
}