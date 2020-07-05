package com.github.mooncoders.jammed.sdk.extensions

import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.EmptyCoroutineContext

suspend fun <T, R> Iterable<T>.parallelMap(
    context: CoroutineContext = EmptyCoroutineContext,
    action: suspend (T) -> R
) = coroutineScope {
    map {
        async(context) { action(it) }
    }.awaitAll()
}