package com.fsryan.tools.dvm

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import java.io.ByteArrayOutputStream
import java.io.IOException

/**
 * Utilities intended to make testing easier by performing relatively esoteric,
 * yet useful procedures on [android.view.View] and other related classes.
 *
 * I took the bulk of it from this gist:
 * [A jist on ryansgot's github page](https://gist.github.com/ryansgot/e68d48947f957d81981135cd9b900e34)
 */
object ViewTestUtil {

    /**
     * Turns a [Drawable] into a [Bitmap]
     */
    fun getBitmap(drawable: Drawable): Bitmap {
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        return bitmap
    }

    /**
     * Gets the [Drawable] of the id passed in--or returns null if it does not
     * exist
     */
    fun drawableById(context: Context, @DrawableRes id: Int): Drawable? {
        val drawable = ContextCompat.getDrawable(context, id) ?: return null
        return if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP)
            DrawableCompat.wrap(drawable).mutate()
            else drawable
    }

    /**
     * Gets the bytes that represent the [Drawable]'s bitmap
     */
    fun bytesOfDrawable(d: Drawable): ByteArray {
        val bitmap = getBitmap(d)
        val stream = ByteArrayOutputStream()
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
        val ret = stream.toByteArray()
        try {
            return ret
        } finally {
            try {
                stream.close()
            } catch (ioe: IOException) {
                throw RuntimeException(ioe)
            }

        }
    }

    /**
     * Resolves the [Drawable] and then gets the bytes that represent its
     * bitmap--returns null if the [Drawable] cannot be resolved by the input
     * [Context]
     */
    fun bytesOfDrawable(context: Context, @DrawableRes id: Int): ByteArray? {
        val drawable = ContextCompat.getDrawable(context, id) ?: return null
        return bytesOfDrawable(drawable)
    }
}