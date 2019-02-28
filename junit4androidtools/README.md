# junit4androidtools

These tools are intended to help you do common andorid and junit4 related things when writing instrumentation tests.

Currently included is:
* [FSAndroidTestFilter](src/main/java/com/fsryan/tools/dvm/junit4/FSAndroidTestFilter.kt)
* [Tools for leaving a record of your data source tests](src/main/java/com/fsryan/tools/dvm/junit4/file_output_rules.kt)

## Using in your project
1. Configure the fs repositories
2. Add this dependency to your `androidTestImplementation` configuration:
    ```groovy
    dependencies {
        /* ... */
        androidTestImplementation 'com.fsryan.tools:junit4androidtools:x.y.z'
    }
    ```

You may need to exclude certain modules depending upon the other things you use in your project

## What is a test filter, and how do I use it?
A test filter is something that narrows down the tests that get run. This test filter is passed as an argument to the android test instrumentation. If you were to do this on the command line, then it would look like this:
```
$ adb am instrument -e filter my.junit4.TestFilter my.package.name/my.test.runner
```
However, the android gradle plugin provides a means of passing these arguments. The following article does a great job of explaining it: https://medium.com/stepstone-tech/exploring-androidjunitrunner-filtering-options-df26d30b4f60

This allows you to write custom annotations and mix/match the combination of tests by:
1. Annotating your tests in some way that you want to: see [TestFilterLongRunningTestMethodDemo](../testtoolsonlibdemo/src/androidTest/java/com/fsryan/tools/dvm/testtoolsonlibdemo/TestFilterLongRunningTestMethodDemo.kt)
2. Applying the correct gradle configuration: see [testtoolsonlibdemo's build.gradle](../testtoolsonlibdemo/build.gradle)

You an mix/match tests by running the appropriate gradle tasks having to do with each test annotation. If you run multiple gradle tasks, then you can run blended test sets.

A plugin is in the works that will go hand-in-hand with the custom annotations and generate the correct tasks and android.testInstrumentationRunnerArguments.
