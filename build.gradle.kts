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
    maven("/usr/local/kotlinxtras/build/xtras/maven")
    google()
    // jcenter()
    mavenCentral()
    //maven("https://s01.oss.sonatype.org/content/groups/staging")


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

  afterEvaluate {
    extensions.findByType(PublishingExtension::class.java)?.apply {
      repositories {
        maven("/usr/local/kotlinxtras/build/xtras/maven"){
          name = "Xtras"
        }
      }
    }
  }
}



