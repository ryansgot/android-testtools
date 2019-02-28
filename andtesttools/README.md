# andtesttools

These tools are intended to help you test on the android device.

Currently included is:
* A [MultiDexInstrumentationRunner](src/main/java/com/fsryan/tools/dvm/MultiDexInstrumentationRunner.kt)
* A [utility for working with LiveData](src/main/java/com/fsryan/tools/dvm/arch/LiveDataUtil.kt)

## Using in your project
1. Configure the fs repositories
2. Add this dependency to your `androidTestImplementation` configuration:
    ```groovy
    dependencies {
        /* ... */
        androidTestImplementation 'com.fsryan.tools:andtesttools:x.y.z'
    }
    ```

You may need to exclude certain modules depending upon the other things you use in your project

## When to use MultiDexInstrumentationRunner
Use [MultiDexInstrumentationRunner](src/main/java/com/fsryan/tools/dvm/MultiDexInstrumentationRunner.kt) when you're testing a library of sufficient complexity. You'll know when you've reached sufficient complexity when the build fails and claims that there are too many method references. When you're testing an app, you don't have as strict limitations on method reference limits.

```groovy
android {
    defaultConfig {
        testInstrumentationRunner "com.fsryan.tools.dvm.MultiDexInstrumentationRunner"
    }
}
```
