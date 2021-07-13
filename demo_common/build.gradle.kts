plugins {
  id("com.android.library")
  id("kotlin-android")
  kotlin("plugin.serialization")
}

android {
  compileSdk = ProjectVersions.SDK_VERSION
  buildToolsVersion = ProjectVersions.BUILD_TOOLS_VERSION

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }
  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }
  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_VERSION
  }
}

dependencies {
  implementation(project(":service"))
  implementation(AndroidX.core.ktx)
  implementation(AndroidX.media2.common)
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  implementation("com.github.danbrough.androidutils:logging_core:_")
  compileOnly(AndroidX.compose.runtime)

  implementation(Square.okHttp3.okHttp)

}