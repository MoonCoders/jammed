package com.github.mooncoders.jammed.ui.foundation

import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import com.google.android.gms.maps.UiSettings

fun UiSettings.changeMyLocationButtonMargin(
    view: View,
    left: Int,
    top: Int,
    right: Int,
    bottom: Int
) {
    val locationButton = view.findViewById<ImageView>(Integer.parseInt("2"))
    val rlp: RelativeLayout.LayoutParams =
        locationButton.layoutParams as RelativeLayout.LayoutParams
    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, 0)
    rlp.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE)
    rlp.setMargins(left, top, right, bottom)
    locationButton.requestLayout()
}