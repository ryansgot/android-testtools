package com.fsryan.tools.dvm.testtoolsonlibdemo

import android.Manifest
import android.support.test.InstrumentationRegistry
import android.support.test.rule.GrantPermissionRule
import android.support.test.runner.AndroidJUnit4
import com.fsryan.tools.dvm.junit4.PrefsTestRule
import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class SharedPrefsExampleLibTest {

    /**
     * The [RuleChain] will enforce execution order on the rules. By
     * experience, many times, the order of the rules in the file is good
     * enough to specify execution order. However, this is how one
     * explicitly specifies it.
     */
    @get:Rule
    val ruleChain = RuleChain.outerRule(GrantPermissionRule.grant(Manifest.permission.WRITE_EXTERNAL_STORAGE))
        /**
         * Note that [InstrumentationRegistry.getContext] is called here instead of
         * [InstrumentationRegistry.getTargetContext]. It is done this way because
         * this is a library project, and library projects only have a test
         * application installed.
         */
        .around(
            PrefsTestRule(
                prefName = SharedPrefsExample.PREF_NAME,
                context = InstrumentationRegistry.getContext()
            )
        )

    private lateinit var prefsUnderTest: SharedPrefsExample

    @Before
    fun initPrefs() {
        prefsUnderTest =
                SharedPrefsExample(InstrumentationRegistry.getContext())
    }

    /**
     * Because the [PrefsTestRule] clears the prefs prior to each run, this
     * test does not depend upon test execution order.
     */
    @Test
    fun shouldHaveFalseAsDefaultBooleanPref() {
        assertFalse(prefsUnderTest.getBooleanPref())
    }

    /**
     * Because the [PrefsTestRule] clears the prefs prior to each run, this
     * test does not depend upon test execution order.
     */
    @Test
    fun shouldReturnEmptyStringAsDefaultStringPref() {
        assertEquals("", prefsUnderTest.getStringPref())
    }

    /**
     * Because the [PrefsTestRule] clears the prefs prior to each run, this
     * test does not depend upon test execution order.
     */
    @Test
    fun shouldReturnEmptySetAsDefaultStringSetPref() {
        assertEquals(setOf<String>(), prefsUnderTest.getStringSetPref())
    }

    @Test
    fun shouldCorrectlyStoreAndRetrieveBooleanPref() {
        prefsUnderTest.storeBooleanPref(true)
        assertTrue(prefsUnderTest.getBooleanPref())
    }

    @Test
    fun shouldCorrectlyStoreAndRetrieveStringPref() {
        prefsUnderTest.storeStringPref("Hello, World!")
        assertEquals("Hello, World!", prefsUnderTest.getStringPref())
    }

    @Test
    fun shouldCorrectlyStoreAndRetrieveStringSetPref() {
        val expected = setOf("this", "is", "quite", "a", "string", "set", "don't", "you", "think?")
        prefsUnderTest.storeStringSetPref(expected)
        assertEquals(expected, prefsUnderTest.getStringSetPref())
    }
}