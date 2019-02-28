package com.fsryan.tools.dvm

import android.os.Bundle
import android.support.multidex.MultiDex
import android.support.test.runner.AndroidJUnitRunner
import android.util.Log


class MultiDexInstrumentationRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        Log.i("RYAN", "running MultiDexInstrumentationRunner")
        for (k in arguments.keySet()) {
            Log.i("RYAN", "arg: $k=${arguments.get(k)}")
        }
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }
}