package com.github.mooncoders.jammed.ui.foundation

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.RecyclerView
import com.github.mooncoders.jammed.R

class SafeRecyclerView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = R.attr.recyclerViewStyle
) : RecyclerView(context, attrs, defStyleAttr) {
    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        adapter = null
    }
}