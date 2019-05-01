package com.fsryan.tools.dvm.espresso

import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.NoMatchingViewException
import android.support.test.espresso.PerformException
import android.support.test.espresso.ViewAction
import android.support.test.espresso.ViewAssertion
import android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition
import android.support.test.espresso.contrib.RecyclerViewActions.scrollToPosition
import android.support.test.espresso.util.HumanReadables
import android.support.v7.widget.RecyclerView
import android.view.View
import org.hamcrest.Matcher
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.`is`

/**
 * copied from here: https://gist.github.com/RomainPiel/ec10302a4687171a5e1a
 * then updated and converted to Kotlin.
 *
 * This class allows you to write assertions on items of the [RecyclerView]
 * more naturally than the espresso-contrib library's RecyclerViewActions class
 * would allow you to do otherwise.
 */
class RecyclerItemViewAssertion<A>(
    private val position: Int,
    private val item: A,
    private val itemViewAssertion: RecyclerViewInteraction.ItemViewAssertion<A>
) :
    ViewAssertion {

    override fun check(view: View, e: NoMatchingViewException) {
        val recyclerView = view as RecyclerView
        val viewHolderForPosition = recyclerView.findViewHolderForLayoutPosition(position)
        if (viewHolderForPosition == null) {
            throw PerformException.Builder()
                .withActionDescription(toString())
                .withViewDescription(HumanReadables.describe(view))
                .withCause(IllegalStateException("No view holder at position: $position"))
                .build()
        } else {
            val viewAtPosition = viewHolderForPosition.itemView
            itemViewAssertion.check(item, viewAtPosition, e)
        }
    }
}

/**
 * Originally copied from
 * [this gist on RomainPiel's github](https://gist.github.com/RomainPiel/ec10302a4687171a5e1a)
 * and then changed to allow for actions to happen to the items prior to checks
 * and for an initial offset and has an [InclusionFilter] that will allow you
 * to only check for items matching the filter.
 *
 * Then updated and converted to Kotlin.
 */
class RecyclerViewInteraction<A> private constructor(private val viewMatcher: Matcher<View>) {
    private var offset = 0
    private var items: List<A> = listOf()
    private val onItemViewActions: MutableList<ViewAction> = mutableListOf()
    private var inclusionFilter: InclusionFilter<A> = object :
        InclusionFilter<A> {
        override fun include(item: A): Boolean {
            return true
        }
    }

    interface InclusionFilter<A> {
        fun include(item: A): Boolean
    }

    fun withOffset(offset: Int): RecyclerViewInteraction<A> {
        this.offset = if (offset >= 0) offset else 0
        return this
    }

    fun withItemInclusionFilter(inclusionFilter: InclusionFilter<A>): RecyclerViewInteraction<A> {
        this.inclusionFilter = inclusionFilter
        return this
    }

    fun withItems(items: List<A>): RecyclerViewInteraction<A> {
        this.items = items
        return this
    }

    fun withOnItemActions(vararg viewActions: ViewAction): RecyclerViewInteraction<A> {
        onItemViewActions.addAll(viewActions)
        return this
    }

    fun check(itemViewAssertion: ItemViewAssertion<A>): RecyclerViewInteraction<A> {
        for (i in items.indices) {
            if (!inclusionFilter.include(items[i])) {
                continue
            }
            val vi = onView(viewMatcher).perform(scrollToPosition<RecyclerView.ViewHolder>(i + offset))
            for (viewAction in onItemViewActions) {
                vi.perform(actionOnItemAtPosition<RecyclerView.ViewHolder>(i + offset, viewAction))
            }
            vi.check(RecyclerItemViewAssertion(i + offset, items[i], itemViewAssertion))
        }
        return this
    }

    interface ItemViewAssertion<A> {
        fun check(item: A, view: View, e: NoMatchingViewException)
    }

    companion object {
        fun <A> onRecyclerView(viewMatcher: Matcher<View>): RecyclerViewInteraction<A> =
            RecyclerViewInteraction(viewMatcher)
    }
}

/**
 * Assert that the count of items in a [RecyclerView] is what you expected.
 */
class RecyclerViewItemCountAssertion(private val expectedCount: Int) : ViewAssertion {

    override fun check(view: View, noViewFoundException: NoMatchingViewException?) {
        if (noViewFoundException != null) {
            throw noViewFoundException
        }

        val recyclerView = view as RecyclerView
        val adapter = recyclerView.adapter
        assertThat<Int>(adapter.itemCount, `is`<Int>(expectedCount))
    }
}