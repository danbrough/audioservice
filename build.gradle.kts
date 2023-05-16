buildscript {

  dependencies {
    classpath("com.android.tools.build:gradle:_")
    classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:_")
  }

  repositories {
    //mavenLocal()
    google()
    mavenCentral()
  }
}


apply("project.gradle.kts")

subprojects {
  repositories {
    google()
    mavenCentral()
    maven("/usr/local/kotlinxtras/build/xtras/maven")
    maven("https://jitpack.io")
  }
}



