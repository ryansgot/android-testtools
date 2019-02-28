# espressotools

These tools are intended to help you test UI on the android device

Currently included is:
* [DrawableMatcher](src/main/java/com/fsryan/tools/dvm/espresso/DrawableMatcher.kt)
* [Tools for working with RecyclerView](src/main/java/com/fsryan/tools/dvm/espresso/recycler_view_tools.kt)
* [A tool for helping you work with drawables and bitmaps](src/main/java/com/fsryan/tools/dvm/ViewTestUtil.kt)

## Using in your project
1. Configure the fs repositories
2. Add this dependency to your `androidTestImplementation` configuration:
    ```groovy
    dependencies {
        /* ... */
        androidTestImplementation 'com.fsryan.tools:espressotesttools:x.y.z'
    }
    ```

You may need to exclude certain modules depending upon the other things you use in your project

## Are there any examples of how to use this stuff?
Not yet, but I'll provide them. For now, you can look at the [ViPID repostalker reference implementation](https://github.com/ryansgot/repostalker)
