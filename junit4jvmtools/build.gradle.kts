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
    id("signing")
    id("fsryan-gradle-publishing")
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

    licenseName = "Apache License, Version 2.0"
    licenseUrl = "https://www.apache.org/licenses/LICENSE-2.0.txt"
    licenseDistribution = "repo"

//    releaseRepoUrl = "s3://repo.fsryan.com/release"
//    snapshotRepoUrl = "s3://repo.fsryan.com/snapshot"
//    awsAccessKeyId = if (project.hasProperty("awsMavenAccessKey")) project.property("awsMavenAccessKey").toString() else System.getenv()["AWS_ACCES_KEY_ID"]!!
//    awsSecretKey = if (project.hasProperty("awsMavenSecretKey")) project.property("awsMavenSecretKey").toString() else System.getenv()["AWS_SECRET_KEY"]!!

    releaseRepoUrl = "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
    releaseBasicUser = project.findProperty("com.fsryan.ossrh.release.username")?.toString().orEmpty()
    releaseBasicPassword = project.findProperty("com.fsryan.ossrh.release.password")?.toString().orEmpty()
    snapshotRepoUrl = "https://s01.oss.sonatype.org/content/repositories/snapshots/"
    snapshotBasicUser = project.findProperty("com.fsryan.ossrh.snapshot.username")?.toString().orEmpty()
    snapshotBasicPassword = project.findProperty("com.fsryan.ossrh.snapshot.password")?.toString().orEmpty()
    useBasicCredentials = true
    useBasicCredentials = true
    description = "Some basic test tools for JUnit4--specifically some assertions on collections"
}

signing {
    if (project.hasProperty("signing.keyId")) {
        if (project.hasProperty("signing.password")) {
            if (project.hasProperty("signing.secretKeyRingFile")) {
                sign(publishing.publications)
            } else {
                println("Missing signing.secretKeyRingFile: cannot sign ${project.name}")
            }
        } else {
            println("Missing signing.password: cannot sign ${project.name}")
        }
    } else {
        println("Missing signing.keyId: cannot sign ${project.name}")
    }
}