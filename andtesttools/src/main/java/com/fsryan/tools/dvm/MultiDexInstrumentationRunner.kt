package com.fsryan.tools.dvm

import android.os.Bundle
import androidx.multidex.MultiDex
import androidx.test.runner.AndroidJUnitRunner
import android.util.Log


class MultiDexInstrumentationRunner : AndroidJUnitRunner() {
    override fun onCreate(arguments: Bundle) {
        Log.i("andtesttools", "running MultiDexInstrumentationRunner")
        for (k in arguments.keySet()) {
            Log.i("andtesttools", "arg: $k=${arguments.get(k)}")
        }
        MultiDex.install(targetContext)
        super.onCreate(arguments)
    }
}