import deps.Deps.mainDep
import deps.Deps.testDep
import deps.Deps.ver
import org.jetbrains.kotlin.gradle.dsl.KotlinJvmOptions

plugins {
    java
    id("kotlin")
    id("maven-publish")
    id("fsryan-gradle-publishing")
}

group = "com.fsryan.testtools.jvm"
version = "0.0.3"

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
}
