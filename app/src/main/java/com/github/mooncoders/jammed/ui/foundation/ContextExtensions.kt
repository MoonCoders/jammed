package com.github.mooncoders.jammed.ui.foundation

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory


/**
 *
 * @author kristiyan
 */
/**
 * Demonstrates converting a [Drawable] to a [BitmapDescriptor],
 * for use as a marker icon.
 */
fun Context.vectorToBitmap(@DrawableRes id: Int, @ColorInt color: Int? = null): BitmapDescriptor? {
    val vectorDrawable =
        ResourcesCompat.getDrawable(resources, id, null)
    val bitmap = Bitmap.createBitmap(
        vectorDrawable!!.intrinsicWidth,
        vectorDrawable.intrinsicHeight, Bitmap.Config.ARGB_8888
    )
    val canvas = Canvas(bitmap)
    vectorDrawable.setBounds(0, 0, canvas.width, canvas.height)
    color?.let { DrawableCompat.setTint(vectorDrawable, it) }
    vectorDrawable.draw(canvas)
    return BitmapDescriptorFactory.fromBitmap(bitmap)
}