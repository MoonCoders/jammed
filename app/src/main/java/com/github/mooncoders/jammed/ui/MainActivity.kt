package com.github.mooncoders.jammed.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.github.mooncoders.jammed.R
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.place_info_sheet.*


class MainActivity : AppCompatActivity() {

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottom_nav_bar.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.map -> {
                }
                R.id.saved -> {

                    bottom_sheet_title.text = getString(R.string.saved)
                }
                R.id.places -> {
                    bottom_sheet_title.text = getString(R.string.places)
                }
                R.id.more -> {
                    bottom_sheet_title.text = getString(R.string.more)
                }
            }
            true
        }

        findNavController(R.id.nav_host_fragment).let {
            bottom_nav_bar.setupWithNavController(it)
        }
    }

}