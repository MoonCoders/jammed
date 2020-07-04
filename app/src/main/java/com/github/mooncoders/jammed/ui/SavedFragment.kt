package com.github.mooncoders.jammed.ui

import android.os.Bundle
import android.view.View
import com.github.mooncoders.jammed.ui.foundation.RecyclerAdapterFragment
import com.github.mooncoders.jammed.ui.items.Cell
import com.github.mooncoders.jammed.ui.items.Title


class SavedFragment : RecyclerAdapterFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        render(
            Title(title = "Test"),
            Cell(
                model = Cell.Model("title", "subtitle"),
                onClick = {

                }
            )
        )
    }
}