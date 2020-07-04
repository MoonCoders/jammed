package com.github.mooncoders.jammed.ui

import android.os.Bundle
import android.view.Gravity
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.view.ContextThemeWrapper
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.mooncoders.jammed.R
import com.github.mooncoders.jammed.sdk.models.PlaceCategory
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.place_info_sheet.*


class MainActivity : AppCompatActivity() {

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<*>

    private val viewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        bottomSheetBehavior = BottomSheetBehavior.from(bottomSheet)
        bottomSheetBehavior.addBottomSheetCallback(object :
            BottomSheetBehavior.BottomSheetCallback() {
            override fun onSlide(bottomSheet: View, slideOffset: Float) {
                if (preview.visibility == View.VISIBLE && slideOffset >= 0)
                    preview.alpha = slideOffset
            }

            override fun onStateChanged(bottomSheet: View, newState: Int) = Unit
        })

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

        viewModel.selectedPointOfInterest.observe(this, Observer { selectedPoi ->
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_HIDDEN
            bottom_sheet_title.text = selectedPoi.title
            bottom_sheet_title.gravity = Gravity.LEFT
            bottom_sheet_subtitle.text = selectedPoi.address

            chips.removeAllViews()

            val live =
                Chip(ContextThemeWrapper(this, R.style.Widget_MaterialComponents_Chip_Action))
            live.setChipBackgroundColorResource(R.color.live_background)
            live.setText(R.string.live)
            live.setTextColor(ContextCompat.getColor(this, R.color.live_text))
            live.chipIcon = getDrawable(R.drawable.ic_live)
            chips.addView(live)

            selectedPoi.categories.forEach {
                val chip =
                    Chip(ContextThemeWrapper(this, R.style.Widget_MaterialComponents_Chip_Action))
                when (it) {
                    PlaceCategory.Beach -> {
                        chip.text = getString(R.string.beach)
                        chip.chipIcon = getDrawable(R.drawable.ic_beach)
                    }
                    PlaceCategory.TouristAttraction -> {
                        chip.text = getString(R.string.tourist)
                        chip.chipIcon = getDrawable(R.drawable.ic_flag)
                    }
                }
                chips.addView(chip)
            }
            Glide.with(this).load(selectedPoi.provider.imageUrl).into(preview)
            bottomSheetBehavior.state = BottomSheetBehavior.STATE_EXPANDED
            preview.visibility = View.VISIBLE
        })
    }

}