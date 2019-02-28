package com.fsryan.tools.jvm.junit4

import com.fsryan.tools.jvm.ReflectionHelper
import org.junit.runner.Description
import org.junit.runner.manipulation.Filter
import java.lang.reflect.Method

@Target(allowedTargets = [AnnotationTarget.CLASS, AnnotationTarget.FUNCTION])
annotation class FSIntegrationTest

@Target(allowedTargets = [AnnotationTarget.CLASS, AnnotationTarget.FUNCTION])
annotation class FSLongRunningTest

/**
 * Base filter for adding custom annotations to test filtering. This is useful
 * to allow different kinds of test runs so that some tests (integration tests)
 * are not run unless explicitly requested. Other tests (uncategorized tests,
 * or tests that you have not annotated specially), however, will run without
 * any special instruction.
 */
abstract class FSTestFilter(protected val allowIntegration: Boolean,
                            protected val allowLongRunning: Boolean,
                            protected val allowUncategorized: Boolean) : Filter() {

    constructor() : this(true, true, true)

    companion object {
        const val FS_INTEGRATION = "fs_integration"
        const val FS_LONG_RUNNING = "fs_long_running"
        const val FS_UNCATEGORIZED = "fs_uncategorized"
    }

    override fun shouldRun(description: Description): Boolean {
        return passesClassFilter(description) && passesMethodFilter(description)
    }

    override fun describe(): String = "allow uncategorized tests? $allowUncategorized; allow integration tests? $allowIntegration"

    private fun passesClassFilter(description: Description): Boolean {
        if (description.testClass == null) {
            // when methodName is null, we cannot correctly evaluate because we
            // don't know what's running. By experiment, returning false here will
            // crash your test run
            return true
        }

        return evaluateFilter(
            description.testClass.isAnnotationPresent(FSIntegrationTest::class.java),
            description.testClass.isAnnotationPresent(FSLongRunningTest::class.java)
        )
    }

    private fun passesMethodFilter(description: Description): Boolean {
        // when methodName is null, we cannot correctly evaluate because we
        // don't know what's running. By experiment, returning false here will
        // crash your test run
        var methodName = JUnit4Utils.methodNameOfTest(description) ?: return true

        // when methodName is null, we cannot correctly evaluate because we
        // don't know what's running. By experiment, returning false here will
        // crash your test run
        val method = ReflectionHelper.getMethodRecursivelyByNameSafe(description.testClass, methodName) ?: return true
        return evaluateFilter(
            classOrMethodHasAnnotation(FSIntegrationTest::class.java, description.testClass, method),
            classOrMethodHasAnnotation(FSLongRunningTest::class.java, description.testClass, method)
        )
    }

    private fun evaluateFilter(isIntegrationTest: Boolean, isLongRunningTest: Boolean): Boolean {
        if (isIntegrationTest && isLongRunningTest) {
            return allowIntegration || allowLongRunning
        }
        if (isIntegrationTest) {
            return allowIntegration
        }
        if (isLongRunningTest) {
            return allowLongRunning
        }
        return allowUncategorized
    }

    private fun classOrMethodHasAnnotation(annotationCls: Class<out Annotation>, targetCls: Class<*>, method: Method?): Boolean {
        val clsHas = targetCls.isAnnotationPresent(annotationCls)
        return if (method == null)  clsHas else clsHas || method.isAnnotationPresent(annotationCls)
    }
}