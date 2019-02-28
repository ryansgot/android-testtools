# jvmtesttools

These tools are intended to help you do common java tasks when writing tests

Currently included is:
* [ReflectionHelper](src/main/java/com/fsryan/tools/jvm/ReflectionHelper.java)

## Using in your project
1. Configure the fs repositories
2. Add this dependency to your `testImplementation` configuration:
    ```groovy
    dependencies {
        /* ... */
        testImplementation 'com.fsryan.tools.jvm:jvmtesttools:x.y.z'
    }
    ```

## What do I do with this?
You can currently only get some method and field information from a class reflectively.
