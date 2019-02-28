# junit4jvmtools

These tools are intended to help you do common junit4 related things when writing jvm tests.

Currently included is:
* [FSTestFilter](src/main/java/com/fsryan/tools/jvm/junit4/test_filtering.kt)
* [Some general JUnit4 utilities](src/main/java/com/fsryan/tools/jvm/junit4/JUnit4Utils.kt)

## Using in your project
1. Configure the fs repositories
2. Add this dependency to your `testImplementation` configuration:
    ```groovy
    dependencies {
        /* ... */
        testImplementation 'com.fsryan.tools.jvm:junit4jvmtools:x.y.z'
    }
    ```


## What do I do with this?
Not much at the moment. It currently supports the [junit4androidtools](../junit4androidtools) project--especially with regard to test filtering. However, if you want to add any tooling here that is specific to the JVM and JUnit4, then you can.
