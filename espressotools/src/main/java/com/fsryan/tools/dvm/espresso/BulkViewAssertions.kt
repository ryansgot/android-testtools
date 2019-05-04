package com.fsryan.tools.dvm.espresso

import android.support.test.espresso.ViewInteraction
import android.support.test.espresso.assertion.ViewAssertions
import android.view.View
import org.hamcrest.Matcher

object BulkViewAssertions {

    fun allMatch(matcher: Matcher<View>, vararg viewInteractions: ViewInteraction) = viewInteractions.forEach { vi ->
        vi.check(ViewAssertions.matches(matcher))
    }

    fun oneMatches(matcher: Matcher<View>, vararg viewInteractions: ViewInteraction) = viewInteractions.firstOrNull { vi ->
        try {
            vi.check(ViewAssertions.matches(matcher))
            return@firstOrNull true
        } catch (e: Exception) {
            return@firstOrNull false
        }
    } != null

    fun noneMatch(matcher: Matcher<View>, vararg viewInteractions: ViewInteraction) = !oneMatches(matcher, *viewInteractions)
}