# android-testtools

Care has been taken to split code bound to the android platform and code that has no android dependency. Therefore, we have `andtesttools` and `jvmtesttools`, for example. When we test, we often test on the JVM, and if we were to add in android dependencies to the mix, we'd tie ourselves down to use of robolectric . . . a brilliant piece of crap that slows down your JVM tests and makes you not want to run them. Projects that use android are free to use the jvm test tools, but not vice-versa. Therefore, to make the tools most useful, you should take put all of the code you can inside of the jvm test tools projects without adding in any android dependency.

## What if I want to use tooling from an internal library?

You should not use it here. Instead, that internal library should have a sibling module that exposes its own test tooling. That internal library's test tooling module is free to depend upon these more generic projects, but not vice versa.

## What tooling goes here:

Lets start by project breakdown:
- [andtesttools](andtesttools)
  * Test tooling that utilizes the android framework (such as getting assets, TelephonyManager, etc). The custom runner that we'll use for library projects is located there.
  * test tooling that utilizes support libraries/androidx/arch
- [espressotools](espressotools)
  * Test tooling that utilizes the android framework--specifically as it relates to espresso for performing verifications on views
- [junit4androidtools](junit4androidtools)
  * Test tooling that utilizes JUnit4 and android framework
- [junit4jvmtools](junit4jvmtools)
  * Test tooling that utilizes JUnit4 but does not utilize the android framework. A good example of this is [FSTestFilter](junit4jvmtools/src/main/java/com/fsryan/tools/jvm/junit4/test_filtering.kt). This has no android dependency, but it is extended in [FSAndroidTestFilter](junit4androidtools/src/main/java/com/fsryan/tools/dvm/junit4/FSAndroidTestFilter.kt) for the version used in android.
- [jvmtesttools](jvmtools)
  * Test tooling that only involves the JVM and no other libraries.

Feel free to add more projects as it appeals to you. Some suggestions are:

1. A gradle plugin that will handle creation of the testing tasks that will filter the tests set, making use of the 

## How do I use this stuff?

I am building in some sample code in the [testtoolsonlibdemo](testtoolsonlibdemo) project. That project is an android library that is intended to demonstrate what you can do with the tools that we have. I'll also build out an android application project to show how to use the tools in an application project.

