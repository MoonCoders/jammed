package com.github.mooncoders.jammed.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.github.mooncoders.jammed.ui.foundation.BaseFragment
import com.github.mooncoders.jammed.ui.items.Cell
import com.github.mooncoders.jammed.ui.items.Title

class FirstFragment : BaseFragment() {

    private val viewModel by lazy {
        ViewModelProvider(requireActivity()).get(MainViewModel::class.java)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.pointsOfInterest.apply {
            success.observe(viewLifecycleOwner, Observer { pointsOfInterest ->
                // TODO draw on map
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })

            fetch(Unit)
        }

        viewModel.pedestriansFetcher.apply {
            success.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Number of pedestrians: $it")
            })

            error.observe(viewLifecycleOwner, Observer {
                Log.e("TAG", "Error", it)
            })
        }

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