package com.github.mooncoders.jammed.ui

import android.os.Bundle
import android.view.View
import com.github.mooncoders.jammed.ui.foundation.RecyclerAdapterFragment
import com.github.mooncoders.jammed.ui.items.EmptyItem


class EmptyFragment : RecyclerAdapterFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        render(
            EmptyItem(
                model = "",
                onClick = {}
            )
        )
    }
}