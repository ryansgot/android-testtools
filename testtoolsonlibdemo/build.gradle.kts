import com.android.build.gradle.LibraryExtension
import com.android.build.gradle.LibraryPlugin
import deps.Deps.mainDep
import deps.Deps.testDep
import deps.Deps.ver
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.library")
    id("kotlin-android")
//    id("fsryan-gradle-publishing")
}

android {
    compileSdkVersion(ver("global", "android", "compileSdk").toInt())

    defaultConfig {
        minSdkVersion(ver("global", "android", "minSdk").toInt())
        targetSdkVersion(ver("global", "android", "targetSdk").toInt())

        // This test instrumentation runner allows libraries of any significant
        // complexity to be tested on the DVM. The underlying issue is the
        testInstrumentationRunner = "com.fsryan.tools.dvm.MultiDexInstrumentationRunner"

        // TODO: put this in a plugin
        // I"ll be looking to make a gradle plugin for this. This sets up the
        // test filtering so that only the "uncategorized" tests will run.
        testInstrumentationRunnerArguments = mapOf(
                "filter" to "com.fsryan.tools.dvm.junit4.FSAndroidTestFilter",
                "fs_integration" to "false",
                "fs_long_running" to "false",
                "fs_uncategorized" to "true"
        )

        multiDexEnabled = true
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    kotlinOptions {
        // Works around an issue in the kotlin-android plugin where the
        // type became Any
        (this as KotlinJvmOptions).jvmTarget = "1.8"
    }

}

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    implementation(mainDep(producer = "jetbrains", name = "kotlin-stdlib"))

    androidTestImplementation(project(":andtesttools"))
    androidTestImplementation(project(":espressotools"))
    androidTestImplementation(project(":junit4androidtools"))

    implementation(testDep(producer = "androidx", name = "monitor"))
    implementation(testDep(producer = "androidx", name = "rules"))
    implementation(testDep(producer = "androidx", name = "espresso-core"))
    implementation(testDep(producer = "androidx", name = "espresso-contrib"))
}


fun requestedTasks(): List<String> = project.gradle.startParameter.taskRequests.map { it.args }.flatten()
fun wasRequestedTask(prefix: String) = requestedTasks().firstOrNull { it == "${project.path}:$prefix" || it == prefix } != null
fun androidLibraryExt() = (project.plugins.findPlugin("com.android.library") as LibraryPlugin).extension as LibraryExtension

project.afterEvaluate {
    // TODO: put this in a plugin
    androidLibraryExt().libraryVariants.forEach { v ->
        val connectedTestTask = project.tasks.findByName("connected${v.name.capitalize()}AndroidTest")
        if (connectedTestTask != null) {
            val longRunningTask = task("fsLongRunning${v.name.capitalize()}AndroidTest")
            longRunningTask.dependsOn(connectedTestTask)
            longRunningTask.group = "verification"
            longRunningTask.description = "Run the long running tests for variant ${v.name}"

            val integrationTestTask = task("fsIntegration${v.name.capitalize()}AndroidTest")
            integrationTestTask.dependsOn(connectedTestTask)
            integrationTestTask.group = "verification"
            integrationTestTask.description = "Run the integration tests for variant ${v.name}"

            val unfilteredTask = task("fsUnfiltered${v.name.capitalize()}AndroidTest")
            unfilteredTask.dependsOn(connectedTestTask)
            unfilteredTask.group = "verification"
            unfilteredTask.description = "Run all connected android tests for variant ${v.name}"

            val unfiltered = wasRequestedTask(unfilteredTask.name)
            val longRunning = wasRequestedTask(longRunningTask.name)
            val integration = wasRequestedTask(integrationTestTask.name)
            if (unfiltered || longRunning || integration) {
                v.mergedFlavor.testInstrumentationRunnerArguments.putAll(
                    mapOf(
                        "filter" to "com.fsryan.tools.dvm.junit4.FSAndroidTestFilter",
                        "fs_integration" to (unfiltered || integration).toString(),
                        "fs_long_running" to (unfiltered || longRunning).toString(),
                        "fs_uncategorized" to (unfiltered || (!longRunning && !integration)).toString()
                    )
                )
            }
        }
    }
}

