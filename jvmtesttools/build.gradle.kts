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

    compileOnly(mainDep(producer = "google", name = "jsr-305"))
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
    description = "Testing tools building on top of the JVM"
    awsAccessKeyId = System.getenv("AWS_ACCESS_KEY_ID") ?: ""
    awsSecretKey = System.getenv("AWS_SECRET_KEY") ?: ""
}