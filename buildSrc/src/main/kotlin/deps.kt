package deps

object Deps {

    private val versions = mapOf(
        "global" to mapOf(
            "jetbrains" to mapOf(
                "kotlin" to "1.3.41"
            ),
            "android" to mapOf(
                "minSdk" to "21",
                "targetSdk" to "28",
                "compileSdk" to "28"
            )
        ),
        "plugins" to mapOf(
            "android" to mapOf(
                "lib" to "3.4.2"
            ),
            "dcendents" to mapOf(
                "android-maven" to "2.1"
            ),
            "fsryan" to mapOf(
                "publishing" to "0.0.2"
            )
        ),
        "main" to mapOf(
            "androidx" to mapOf(
                "annotation" to "1.0.0",
                "lifecycle" to "2.0.0",
                "multidex" to "2.0.0"
            ),
            "google" to mapOf(
                "jsr-305" to "3.0.2"
            )
        ),
        "test" to mapOf(
            "androidx" to mapOf(
                "arch-core" to "2.0.0",
                "core" to "1.2.0",
                "espresso" to "3.2.0",
                "junit" to "1.1.1"
            ),
            "junit" to mapOf(
                "junit4" to "4.12"
            )
        )
    )
    private val deps = mapOf(
        "plugins" to mapOf(
            "android" to mapOf(
                "gradle" to "com.android.tools.build:gradle:${ver("plugins", "android", "lib")}"
            ),
            "dcendents" to mapOf(
                "android-maven" to "com.github.dcendents:android-maven-gradle-plugin:${ver("plugins", "dcendents", "android-maven")}"
            ),
            "fsryan" to mapOf(
                "publishing" to "com.fsryan.gradle:fsryan-gradle-publishing:${ver("plugins", "fsryan", "publishing")}"
            ),
            "jetbrains" to mapOf(
                "kotlin" to "org.jetbrains.kotlin:kotlin-gradle-plugin:${ver("global", "jetbrains", "kotlin")}"
            )
        ),
        "main" to mapOf(
            "androidx" to mapOf(
                "annotation" to "androidx.annotation:annotation:${ver("main", "androidx", "annotation")}",
                "lifecycle-extensions" to "androidx.lifecycle:lifecycle-extensions:${ver("main", "androidx", "lifecycle")}",
                "multidex" to "androidx.multidex:multidex:${ver("main", "androidx", "multidex")}"
            ),
            "google" to mapOf(
                "jsr-305" to "com.google.code.findbugs:jsr305:${ver("main", "google", "jsr-305")}"
            ),
            "jetbrains" to mapOf(
                "kotlin-stdlib" to "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${ver("global", "jetbrains", "kotlin")}"
            )
        ),
        "test" to mapOf(
            "androidx" to mapOf(
                "arch-testing" to "androidx.arch.core:core-testing:${ver("test", "androidx", "arch-core")}",
                "core" to "androidx.test:core:${ver("test", "androidx", "core")}",
                "espresso-core" to "androidx.test.espresso:espresso-core:${ver("test", "androidx", "espresso")}",
                "espresso-contrib" to "androidx.test.espresso:espresso-contrib:${ver("test", "androidx", "espresso")}",
                "junit" to "androidx.test.ext:junit:${ver("test", "androidx", "junit")}",
                "monitor" to "androidx.test:monitor:${ver("test", "androidx", "core")}",
                "rules" to "androidx.test:rules:${ver("test", "androidx", "core")}",
                "runner" to "androidx.test:runner:${ver("test", "androidx", "core")}"
            ),
            "junit" to mapOf(
                "lib" to "junit:junit:${ver("test", "junit", "junit4")}"
            )
        )
    )

    fun ver(domain: String, producer: String, name: String): String = findObj(versions, "versions", domain, producer, name)
    fun dep(domain: String, producer: String, name: String): String  = findObj(deps, "deps", domain, producer, name)
    fun pluginDep(producer: String, name: String) = dep("plugin", producer, name)
    fun testDep(producer: String, name: String) = dep("test", producer, name)
    fun mainDep(producer: String, name: String) = dep("main", producer, name)
    fun findObj(src: Map<String, Map<String, Map<String, String>>>, srcName: String, domain: String, producer: String, name: String): String = src[domain]?.get(producer)?.get(name)
        ?: throw IllegalArgumentException("did not find $srcName.$domain.$producer.$name")
}