package com.fsryan.tools.dvm.testtoolsonlibdemo

import androidx.test.runner.AndroidJUnit4
import com.fsryan.tools.jvm.junit4.FSLongRunningTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Because of the instrumentation runnner arguments set up in the gradle file,
 * only the [shouldRunWhenFilteringLongRunningTests] method will get executed
 * You must run the corresponding fsLongRunning<Variant Name>AndroidTest
 * task in order to get this test to run
 */
@RunWith(AndroidJUnit4::class)
class TestFilterLongRunningTestMethodDemo {

    @FSLongRunningTest
    @Test
    fun shouldNotRunWhenFilteringLongRunningTests() {
        Thread.sleep(10000)
    }

    @Test
    fun shouldRunWhenFilteringLongRunningTests() {
    }
}