import deps.Deps.mainDep
import deps.Deps.testDep
import deps.Deps.ver
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

import java.util.Date
import java.text.SimpleDateFormat

plugins {
    java
    id("kotlin")
    id("maven-publish")
    id("fsryan-gradle-publishing")
    id("com.jfrog.bintray")
}

group = "com.fsryan.testtools.jvm"

java.sourceCompatibility = JavaVersion.VERSION_1_7
java.targetCompatibility = JavaVersion.VERSION_1_7

dependencies {
    implementation(fileTree(mapOf("include" to listOf("*.jar"), "dir" to "libs")))

    implementation(mainDep(producer = "jetbrains", name = "kotlin-stdlib"))
    implementation(testDep(producer = "junit", name = "lib"))

    implementation(project(":jvmtesttools"))
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
    description = "Testing tools for the JVM building on top of JUnit4"
    awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID") ?: ""
    awsSecretKey = System.getenv("AWS_SECRET_KEY") ?: ""
    additionalPublications.add("bintray")
}

bintray {
    user = if (project.hasProperty("bintrayUser")) project.property("bintrayUser").toString() else ""
    key = if (project.hasProperty("bintrayApiKey")) project.property("bintrayApiKey").toString() else ""
    setPublications("mavenToBintray")
    publish = true

    pkg.apply {
        repo = "maven"
        name = project.name
        userOrg = "fsryan"
        desc = "Testing tools for the JVM building on top of JUnit4"
        websiteUrl = "https://github.com/ryansgot/android-testtools/${project.name}"
        issueTrackerUrl = "https://github.com/ryansgot/android-testtools/issues"
        vcsUrl = "https://github.com/ryansgot/android-testtools.git"
        githubRepo = "ryansgot/android-testtools"
        githubReleaseNotesFile = "jvmtesttools/README.md"
        publicDownloadNumbers = true
        setLicenses("Apache-2.0")
        setLabels("jvm", "test", "java", "junit", "junit4")
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