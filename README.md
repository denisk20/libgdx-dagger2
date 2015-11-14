# libGDX with dagger 2 sample project

This is a modified stock libGDX project that uses Dagger 2.

## Things that has been modified comparing to stock libGDX project:

1. Dagger 2 dependency has been introduced in `build.gradle`:
  * `apt` plugin has been added to build classpath - this is needed to be able to avoid including `dagger-compiler` dependency as `compile`, thus, packaging `dagger-compiler` into the game.

    ```
    buildscript {
      ...
      dependencies {
        ...
        classpath "net.ltgt.gradle:gradle-apt-plugin:0.4"
      }
    }
    ```
  * Declaring dagger2 dependencies for `core` subproject:
 
    ```
    ext {
      ...
      daggerVersion = '2.0.2'
    }

    project(":core") {
      apply plugin: "java"
      apply plugin: "net.ltgt.apt"

      dependencies {
          ...
          compile "com.google.dagger:dagger:$daggerVersion"
          apt "com.google.dagger:dagger-compiler:$daggerVersion"
      }
    }
    ```
2. dagger GWT module has been added into `html/src/dagger/Dagger.gwt.xml`. Also, in order for GWT to work, sources for dagger 
core and `javax.inject` packages has been re-packaged under `html/src/dagger/emu` directory and declared 
in the GWT module:
   ```
   <?xml version="1.0" encoding="UTF-8"?>
   <!DOCTYPE module PUBLIC "-//Google Inc.//DTD Google Web Toolkit trunk//EN"     "http://google-web-toolkit.googlecode.com/svn/trunk/distro-source/core/src/gwt-module.dtd">
   <module>
       <source path="" />
       <super-source path="emu"/>
   </module>
   ```
I also need to inherit from this module in `core/src/MyGame.gwt.xml`.

3. `html` module has been modified to add path to generated dagger sources:

   ```
   gwt {
       ...
       src = files(file("src/")) // Needs to be in front of "modules" below.
       src += files(new File(project(":core").projectDir, "/build/generated/source/apt/main"))
   }
   ```

4. In order to run `desktop` I had to remove standard Application configuration in IntelliJ and add Gradle 
configuration:

   ![image](https://cloud.githubusercontent.com/assets/3080318/11165494/b6af7908-8b19-11e5-968b-652e0bd22569.png)

   This is needed in order for IntelliJ to generate dagger sources on running `desktop`. On the very first run it feels very slow, but once gradle daemon warms up it makes little to no difference with traditional Application launcher configuration. It also supports hot swap.

  That's pretty much it. I have tested this configuration on desktop, ios, android, html (both superDev and dist).
If you're aware of a better way of incorporating dagger 2, let me know.
