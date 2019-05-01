package com.fsryan.tools.dvm

import android.os.Bundle
import org.junit.Assert

object FSBundleTestUtil {
    fun assertBundleEquals(expected: Bundle?, actual: Bundle?) {
        if (expected == null && actual == null) {
            return
        }
        if (expected == null || actual == null) {
            Assert.fail("Expected $expected, but was: $actual")
            return
        }

        val extraKeys = mutableSetOf<String>()
        val missingKeys = mutableSetOf<Pair<String, Pair<Any, Any?>>>()
        val differentKeys = mutableSetOf<String>()

        expected.keySet().forEach { expectedKey ->
            val expectedVal: Any = expected.get(expectedKey)!!
            val actualVal = actual.get(expectedKey)
            if (actualVal == null) {
                missingKeys.add(Pair(expectedKey, Pair<Any, Any?>(expectedVal, actualVal)))
            } else if (expectedVal != actualVal) {
                differentKeys.add(expectedKey)
            }
        }
        extraKeys.addAll(actual.keySet().filter { !expected.containsKey(it) })
        if (extraKeys.isEmpty() && missingKeys.isEmpty() && differentKeys.isEmpty()) {
            return
        }
        Assert.fail("extra:     $extraKeys\nmissing:   $missingKeys\ndifferent: $differentKeys")
    }
}