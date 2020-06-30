package com.github.mooncoders.jammed.ui.foundation

import android.view.View
import androidx.paging.PagedList

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