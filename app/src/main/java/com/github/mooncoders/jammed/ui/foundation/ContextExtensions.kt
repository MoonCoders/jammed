package com.github.mooncoders.jammed.ui.foundation

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.Log
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.fragment.app.FragmentActivity
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng


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

fun FragmentActivity.openUrl(url: String) {
    try {
        CustomTabsIntent.Builder().build().apply {
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_DEFAULT_ONLY)
                .firstOrNull { it.activityInfo.packageName == "com.android.chrome" }?.let {
                    intent.setPackage(it.activityInfo.packageName)
                }
        }.launchUrl(this, Uri.parse(url))
    } catch (exception: Throwable) {
        Log.e("TAG", "Error while opening browser on URL: $url", exception)
    }
}

fun FragmentActivity.getDirections(from: LatLng, to: LatLng) {
    val intent = Intent(
        Intent.ACTION_VIEW,
        Uri.parse("http://maps.google.com/maps?saddr=${from.latitude},${from.longitude}&daddr=${to.latitude},${to.longitude}")
    )
    startActivity(intent)
}