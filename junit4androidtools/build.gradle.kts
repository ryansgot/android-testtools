import java.util.Date
import java.text.SimpleDateFormat
import deps.Deps.mainDep
import deps.Deps.testDep
import deps.Deps.ver
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    id("com.android.library")
    id("kotlin-android")
    id("maven-publish")
    id("android-maven")
    id("fsryan-gradle-publishing")
    id("com.jfrog.bintray")
}

group = "com.fsryan.testtools.android"

android {
    compileSdkVersion(ver("global", "android", "compileSdk").toInt())

    defaultConfig {
        minSdkVersion(ver("global", "android", "minSdk").toInt())
        targetSdkVersion(ver("global", "android", "targetSdk").toInt())

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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

    implementation(testDep(producer = "androidx", name = "monitor"))
    implementation(testDep(producer = "androidx", name = "rules"))
    implementation(testDep(producer = "junit", name = "lib"))

    implementation(project(":jvmtesttools"))
    api(project(":junit4jvmtools"))
}

fsPublishingConfig {
    developerName = "Ryan Scott"
    developerId = "fsryan"
    developerEmail = "fsryan.developer@gmail.com"
    siteUrl = "https://github.com/ryansgot/android-testtools"
    baseArtifactId = project.name
    groupId = project.group.toString()
    versionName = project.version.toString()
    releaseRepoUrl = "s3://repo.fsryan.com/release"
    snapshotRepoUrl = "s3://repo.fsryan.com/snapshot"
    description = "JUnit4 tools for android"
    awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID") ?: ""
    awsSecretKey = System.getenv("AWS_SECRET_KEY") ?: ""
    additionalPublications.add("bintray")
}

bintray {
    user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser").toString() else ""
    key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey").toString() else ""
    setPublications("${project.name}ReleaseToBintray")
    publish = false

    pkg.apply {
        repo = "maven"
        name = project.name
        desc = "JUnit4 tools for android"
        websiteUrl = "https://github.com/ryansgot/android-testtools/${project.name}"
        issueTrackerUrl = "https://github.com/ryansgot/android-testtools/issues"
        vcsUrl = "https://github.com/ryansgot/android-testtools.git"
        publicDownloadNumbers = true
        setLicenses("Apache-2.0")
        setLabels("test", "android", "junit", "junit4")
        version.apply {
            name = project.version.toString()
            released = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZZ").format(Date())
            vcsTag = "v${project.version}"
        }
    }
}

project.afterEvaluate {
    checkNotNull(project.tasks.findByName("release")).dependsOn(checkNotNull(project.tasks.findByName("bintrayUpload")))
}