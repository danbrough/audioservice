plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  //kotlin("serialization")
  kotlin("plugin.serialization")

}



android {

  compileSdk = ProjectVersions.SDK_VERSION
  ndkVersion = ProjectVersions.NDK_VERSION
  buildToolsVersion = ProjectVersions.BUILD_TOOLS_VERSION

  defaultConfig {
    //buildToolsVersion("30.0.2")

    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")

    vectorDrawables {
      useSupportLibrary = false
    }
  }

  lint {
    isAbortOnError = false
  }
  buildFeatures {
    viewBinding = true
    compose = true
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

  kapt {
    correctErrorTypes = true
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

tasks.withType<Test> {
  useJUnit()

  testLogging {
    events("standardOut", "started", "passed", "skipped", "failed")
    showStandardStreams = true
    outputs.upToDateWhen {
      false
    }
  }
}

dependencies {

  implementation(project(":service"))
  implementation(project(":demo_common"))

  implementation(AndroidX.lifecycle.runtimeKtx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.lifecycle.viewModelKtx)
  //implementation(AndroidX.coreKtx)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)
  implementation(AndroidX.media2.common)

  implementation("androidx.core:core-ktx:_")
  implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:_")
  //implementation("org.jetbrains.kotlinx:kotlinx-serialization-cbor:_")
  //implementation("org.jetbrains.kotlinx:kotlinx-serialization-protobuf:_")


  implementation("androidx.navigation:navigation-compose:2.4.0-alpha04")
  implementation("androidx.activity:activity-compose:1.3.0-rc01")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:1.0.0-alpha07")
  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.ui.text)
  implementation(AndroidX.compose.ui.util)
  implementation("androidx.compose.ui:ui-viewbinding:_")
  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.compose.runtime.liveData)
  implementation(AndroidX.compose.material)
  implementation(AndroidX.compose.material.icons.extended)
  implementation("androidx.compose.ui:ui-tooling:${ProjectVersions.COMPOSE_TOOLS_VERSION}")
  implementation("com.github.danbrough.androidutils:compose:_")
  implementation("com.github.fornewid:material-motion-compose:0.5.1-dan")
  implementation("com.google.accompanist:accompanist-systemuicontroller:_")
  implementation("com.google.accompanist:accompanist-insets:_")
  implementation("com.google.accompanist:accompanist-coil:_")

  //implementation("androidx.media2:media2-exoplayer:$media_version")
  //implementation("androidx.media2:media2-player:$media_version")
  //implementation("com.google.guava:guava:_")

  implementation("com.github.danbrough.androidutils:logging_android:_")
  //implementation("com.github.danbrough.androidutils:menu:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  implementation("com.github.danbrough.androidutils:compose:_")

/*  implementation("com.mikepenz:iconics-core:5.0.3")
  implementation("com.mikepenz:iconics-views:5.0.3")
  implementation ("com.mikepenz:fontawesome-typeface:5.9.0.0-kotlin")
  implementation("com.mikepenz:community-material-typeface:5.3.45.1-kotlin")*/



  implementation(Square.okHttp3.okHttp)


//  implementation(project(":menu"))

  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)


/*  testImplementation("ch.qos.logback:logback-classic:_")
  testImplementation("ch.qos.logback:logback-core:_")

  kapt("com.google.dagger:hilt-android-compiler:_")
  implementation("com.google.dagger:hilt-android:_")

  // For instrumentation tests
  androidTestImplementation("com.google.dagger:hilt-android-testing:_")

  // For local unit tests
  testImplementation("com.google.dagger:hilt-android-testing:_")*/


}
