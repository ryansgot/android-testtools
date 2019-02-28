package com.fsryan.tools.jvm.junit4

import org.junit.runner.Description

class JUnit4Utils {
    companion object {
        @JvmStatic
        fun methodNameOfTest(description: Description): String? {
            var ret = description.methodName
            if (ret.isNullOrEmpty()) {
                return ret
            }

            // evaluates method name, stipping the parameterized portion of the
            // method name as it appears in the description
            val paramRunMarkerIdx = ret.indexOf('[')
            return if (paramRunMarkerIdx < 0) ret else ret.substring(0, paramRunMarkerIdx)
        }
    }
}