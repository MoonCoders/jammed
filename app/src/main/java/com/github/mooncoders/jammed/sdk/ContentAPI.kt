package com.github.mooncoders.jammed.sdk

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Url

interface ContentAPI {
    @GET
    suspend fun getData(@Url url: String): ResponseBody
}