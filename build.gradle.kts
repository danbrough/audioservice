buildscript {


  dependencies {
    //classpath(Android.tools.build.gradlePlugin)
    classpath(Android.tools.build.gradlePlugin)
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")


  }

  repositories {
    //mavenLocal()
    google()
    mavenCentral()
    // jcenter()
  }

}


apply("project.gradle.kts")

subprojects {

  repositories {
    //mavenLocal()
    google()
    // jcenter()
    mavenCentral()
    maven("/usr/local/kotlinxtras/build/xtras/maven/")
    //maven("https://h1.danbrough.org/maven/")
    maven("https://jitpack.io")
    //  mavenLocal()
  }
  /*configurations.all {
    if (name.toLowerCase().contains("test")) {
      resolutionStrategy.dependencySubstitution {
        substitute(module(Libs.slf4j)).with(module(Libs.logback_classic))
      }
    }
  }*/


}



