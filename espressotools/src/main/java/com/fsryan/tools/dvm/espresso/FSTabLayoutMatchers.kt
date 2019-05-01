package com.fsryan.tools.dvm.espresso

import android.support.annotation.IdRes
import android.support.annotation.StringRes
import android.support.design.widget.TabLayout
import android.support.test.espresso.matcher.ViewMatchers
import android.view.View
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers
import org.hamcrest.TypeSafeMatcher

object FSTabLayoutMatchers {
    /**
     * Matches a [TabLayout] based upon the selected position
     */
    fun tabLayoutSelectedPositionMatcher(selectedPosition: Int): Matcher<View> = tabLayoutSelectionMatcher(true, selectedPosition)

    /**
     * Matches a [TabLayout] based upon the unselected position
     */
    fun tabLayoutUnselectedPositionMatcher(selectedPosition: Int): Matcher<View> = tabLayoutSelectionMatcher(false, selectedPosition)

    /**
     * Matches a [TabLayout] based upon the selected or unselected position,
     * depending upon [expectedSelected]
     */
    fun tabLayoutSelectionMatcher(expectedSelected: Boolean, position: Int): Matcher<View> {
        return object: TypeSafeMatcher<View>() {
            private var reason: String? = null

            @Override
            override fun matchesSafely(item: View): Boolean {
                if (item !is TabLayout) {
                    reason = "expected view to be a ${TabLayout::class.java.name}, but got a: ${item.javaClass.name}";
                    return false
                }
                if (expectedSelected && item.selectedTabPosition != position) {
                    reason = "expected selected position to be $position, but was: ${item.selectedTabPosition}"
                    return false
                }
                if (!expectedSelected && item.selectedTabPosition == position) {
                    reason = "expected selected position to NOT be $position, but was: $position"
                    return false
                }
                return true
            }

            @Override
            override fun describeTo(description: Description) {
                description.appendText(reason ?: "")
            }
        }
    }

    /**
     * Matches a tab of a [TabLayout] based upon the label's text
     */
    fun matchTab(@StringRes labelRes: Int, @IdRes parentId: Int): Matcher<View> =
        Matchers.allOf(ViewMatchers.withText(labelRes), ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentId)))

    /**
     * Matches a tab of a [TabLayout] based upon the label's text
     */
    fun matchTab(label: String, @IdRes parentId: Int): Matcher<View> =
        Matchers.allOf(ViewMatchers.withText(label), ViewMatchers.isDescendantOfA(ViewMatchers.withId(parentId)))
}