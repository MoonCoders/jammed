package com.github.mooncoders.jammed.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.github.mooncoders.jammed.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.place_info_sheet.*

class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)

        bottom_nav_bar.setOnNavigationItemSelectedListener { item ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
            when (item.itemId) {
                R.id.explore -> {
                    bottom_sheet_title.text = getString(R.string.explore)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                }
                R.id.saved -> {
                    bottom_sheet_title.text = getString(R.string.saved)
                }
                R.id.places -> {
                    bottom_sheet_title.text = getString(R.string.places)
                }
                R.id.more -> {
                    bottom_sheet_title.text = getString(R.string.more)
                    bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
                }
            }
            true
        }

//        setupActionBarWithNavController(findNavController(R.id.nav_host_fragment))
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp()
    }

}