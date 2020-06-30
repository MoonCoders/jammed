package com.github.mooncoders.jammed.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.github.mooncoders.jammed.ui.foundation.BaseFragment
import com.github.mooncoders.jammed.ui.items.Cell
import com.github.mooncoders.jammed.ui.items.Title

class FirstFragment : BaseFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        render(
            Title("Hello world"),
            Cell(
                model = Cell.Model("cell title", "cell subtitle"),
                onClick = {
                    Toast.makeText(requireContext(), "Bravo!", Toast.LENGTH_SHORT).show()
                }
            )
        )
    }
}