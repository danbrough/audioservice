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

  buildFeatures {
    viewBinding = true
    compose = true
  }

  buildTypes {
    release {
      isMinifyEnabled = ProjectVersions.MINIFY_ENABLED
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

  composeOptions {
    kotlinCompilerExtensionVersion = ProjectVersions.COMPOSE_VERSION
  }

  kotlin.sourceSets.all {
    setOf(
        "kotlinx.serialization.ExperimentalSerializationApi",
        "androidx.compose.material.ExperimentalMaterialApi",
        "androidx.compose.animation.ExperimentalAnimationApi",
        "kotlin.time.ExperimentalTime",
        //"kotlinx.coroutines.ExperimentalCoroutinesApi",
        //"kotlinx.coroutines.FlowPreview",
        //"androidx.compose.material.ExperimentalMaterialApi"
    ).forEach {
      languageSettings.useExperimentalAnnotation(it)
    }
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
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")
  implementation(AndroidX.lifecycle.viewModelKtx)
  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.constraintLayoutCompose)
  implementation(AndroidX.compose.material)

  implementation(Google.android.material)
  api("androidx.media2:media2-common:_")
  api("androidx.media2:media2-session:_")

  implementation("androidx.navigation:navigation-compose:_")
  implementation("androidx.activity:activity-compose:_")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:_")

}