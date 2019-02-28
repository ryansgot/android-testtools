package com.fsryan.tools.dvm.junit4

import android.os.Bundle
import android.util.Log
import com.fsryan.tools.jvm.junit4.FSTestFilter
import org.junit.runner.Description

class FSAndroidTestFilter(arguments: Bundle) : FSTestFilter(
            allowIntegration = booleanFromString(arguments, FS_INTEGRATION, false),
            allowLongRunning = booleanFromString(arguments, FS_LONG_RUNNING, false),
            allowUncategorized = booleanFromString(arguments, FS_UNCATEGORIZED, true)) {
    init {
        for (k in arguments.keySet()) {
            Log.i("FSAndroidTestFilter", "instrumentation test argument $k=${arguments.get(k)}")
        }
    }

    override fun shouldRun(description: Description): Boolean {
        val ret = super.shouldRun(description)
        Log.i("FSAndroidTestFilter", "should run test: ${description.testClass.name}.${description.methodName}? $ret")
        return ret
    }

    companion object {
        /**
         * The type of each object on the arguments bundle is String. Thus, you
         * you must perform the deserialization yourself here.
         */
        private fun booleanFromString(b: Bundle, key: String, defaultValue: Boolean): Boolean {
            return b.getString(key, defaultValue.toString())?.toBoolean() ?: defaultValue
        }
    }
}