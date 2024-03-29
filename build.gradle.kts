buildscript {
    repositories {
        maven { url = uri("https://plugins.gradle.org/m2/") }
        google()
        mavenCentral()
        maven { url = uri("https://dl.bintray.com/ryansgot/maven") }
        maven {
            url = uri("s3://repo.fsryan.com/release")
            authentication {
                val awsIm by registering(AwsImAuthentication::class)
            }
        }
        maven {
            url = uri("s3://repo.fsryan.com/snapshot")
            authentication {
                val awsIm by registering(AwsImAuthentication::class)
            }
        }
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.4.2")
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.3.41")
        classpath("com.github.dcendents:android-maven-gradle-plugin:2.1")
        classpath("com.fsryan.gradle:fsryan-gradle-publishing:0.1.3")
        classpath("com.jfrog.bintray.gradle:gradle-bintray-plugin:1.8.4")
    }
}

allprojects {
    version = "0.0.4"

    repositories {
        google()
        mavenCentral()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
}
