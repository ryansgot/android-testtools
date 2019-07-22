package com.fsryan.tools.dvm.espresso

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.DrawableRes
import androidx.test.InstrumentationRegistry.getTargetContext
import android.view.View
import android.widget.ImageView
import com.fsryan.tools.dvm.ViewTestUtil
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

/**
 * A view matcher for making assertions that a view containing a [Drawable]
 * contains the correct drawable. It works best on icons and such.
 *
 * You can expect that the drawables have a color filter applied. The
 * [matchesSafely] function will apply the color filter to a copy of the
 * expected drawable before performing the equivalence verification.
 *
 * The equivalence verification is done by calling
 * [android.graphics.Bitmap.sameAs] on bitmaps generated from the actual
 * [Drawable] extracted from the matched [View] (the target) and on the
 * expected [Drawable] with the input [ColorFilter] ([expectedColorFilter])
 * applied if non-null.
 *
 * I took the bulk of it from this gist:
 * [A jist on ryansgot's github page](https://gist.github.com/ryansgot/e68d48947f957d81981135cd9b900e34)
 * then I converted it to Kotlin.
 */
class DrawableMatcher(
    @param:DrawableRes @field:DrawableRes private val drawableId: Int,
    private val expectedDrawableContext: Context = getTargetContext(),
    private val expectedColorFilter: ColorFilter? = null,
    private val extractorFunction: (View) -> Drawable? = { (it as ImageView).drawable }) : TypeSafeMatcher<View>() {
    private var reason: String? = null

    interface Extractor {
        fun extract(v: View): Drawable?

        companion object {
            val FOR_IMAGE_VIEW: Extractor = object :
                Extractor {
                override fun extract(v: View): Drawable {
                    return (v as ImageView).drawable
                }
            }
        }
    }

    override fun matchesSafely(target: View): Boolean {
        val actualDrawable = extractorFunction(target)
        if (drawableId < 0 && actualDrawable != null) {
            reason = "expected no drawable for view " + target.id + ", but has one"
            return false
        }

        if (actualDrawable == null) {
            if (drawableId < 0) {
                return true
            }
            reason = "Unsuccessful extraction of drawable $drawableId"
            return false
        }

        val expectedDrawable = ViewTestUtil.drawableById(expectedDrawableContext, drawableId)
        if (expectedDrawable == null) {
            reason = "drawable with id $drawableId does not exist"
            return false
        }
        expectedDrawable.colorFilter = expectedColorFilter

        val expected = ViewTestUtil.getBitmap(expectedDrawable)
        val actual = ViewTestUtil.getBitmap(actualDrawable)
        if (!expected.sameAs(actual)) {
            reason = "expected and actual bitmaps do not match"
            return false
        }
        return true
    }

    override fun describeTo(description: Description) {
        description.appendText(if (reason == null) "" else reason)
    }

    companion object {

        @JvmStatic
        fun ofNull(): DrawableMatcher =
            of(-1)

        @JvmOverloads
        fun ofImageView(@DrawableRes drawableId: Int,
                        expectedDrawableContext: Context = getTargetContext(),
                        expectedColorFilter: ColorFilter? = null): DrawableMatcher {
            return of(
                drawableId,
                expectedDrawableContext,
                expectedColorFilter
            )
        }

        @JvmOverloads
        fun of(@DrawableRes drawableId: Int,
               expectedDrawableContext: Context = getTargetContext(),
               expectedColorFilter: ColorFilter? = null,
               extractor: Extractor = Extractor.FOR_IMAGE_VIEW
        ): DrawableMatcher {
            return DrawableMatcher(
                drawableId,
                expectedDrawableContext,
                expectedColorFilter
            ) { extractor.extract(it) }
        }
    }
}