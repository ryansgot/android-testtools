package com.fsryan.tools.dvm.testtoolsonlibdemo

import android.support.test.runner.AndroidJUnit4
import com.fsryan.tools.jvm.junit4.FSLongRunningTest
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Because of the instrumentation runnner arguments set up in the gradle file,
 * tests in this class will not get run on a normal connectedAndroidTest run.
 * You must run the corresponding fsLongRunning<Variant Name>AndroidTest task
 * in order to get this test to run
 */
@FSLongRunningTest
@RunWith(AndroidJUnit4::class)
class TestFilterLongRunningTestDemo {

    @Test
    fun shouldRunForALongTime() {
        Thread.sleep(10000)
    }

    @Test
    fun shouldRunForALongerTime() {
        Thread.sleep(100000)
    }
}