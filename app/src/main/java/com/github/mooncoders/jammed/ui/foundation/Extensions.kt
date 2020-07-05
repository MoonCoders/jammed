package com.github.mooncoders.jammed.ui.foundation

import android.view.View
import androidx.paging.PagedList
import com.github.mooncoders.jammed.sdk.models.CrowdIndicator
import java.security.SecureRandom

fun View.visible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}

fun defaultPagedList() = PagedList.Config.Builder()
    .setPageSize(50)
    .setPrefetchDistance(10)
    .setEnablePlaceholders(false) // removing this crashes the PagingAdapter
    .build()

inline fun <T, reified R> T?.applyOrEmpty(block: T.() -> Array<R>): Array<R> {
    if (this == null) return emptyArray()
    return block(this)
}

private val random: SecureRandom = SecureRandom()

fun <T : Enum<*>?> randomEnum(clazz: Class<T>): T {
    val x: Int = random.nextInt(clazz.enumConstants!!.size)
    return clazz.enumConstants!![x]
}