package com.fsryan.tools.jvm.junit4

import org.junit.runner.Description
import java.lang.IllegalStateException
import java.lang.reflect.Method

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

        @JvmStatic
        fun testMethod(description: Description): Method {
            val methodName: String = methodNameOfTest(description) ?: throw IllegalStateException("could not find method name for description: $description")
            return description.testClass.getDeclaredMethod(methodName)
        }

        @JvmStatic
        fun <T: Annotation> testMethodAnnotation(description: Description, aCls: Class<T>): T? = testMethod(description).getAnnotation(aCls)
    }
}