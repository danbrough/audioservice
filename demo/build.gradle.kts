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
  }

  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }


  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_VERSION
  }

  kapt {
    correctErrorTypes = true
  }

  kotlin.sourceSets.all {
    setOf(
        "kotlinx.serialization.ExperimentalSerializationApi",
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

  implementation(project(":demo_common"))
  implementation(project(":service"))
  implementation(AndroidX.lifecycle.runtime.ktx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.lifecycle.viewModelKtx)
  //implementation(AndroidX.coreKtx)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)
  implementation(AndroidX.core.ktx)
  implementation(KotlinX.serialization.json)
  //implementation(KotlinX.serialization.cbor)
  //implementation(KotlinX.serialization.protobuf)
  implementation(AndroidX.palette.ktx)

  implementation(AndroidX.navigation.fragmentKtx)
  implementation(AndroidX.navigation.uiKtx)
  implementation(AndroidX.constraintLayout)
  implementation(AndroidX.preference.ktx)
  implementation(AndroidX.media2.common)
  implementation(AndroidX.concurrent.futures)
  implementation(Google.android.material)

  //implementation(AndroidX.media2.exoplayer)
  //implementation(AndroidX.media2.player)
  //implementation("com.google.guava:guava:_")

  implementation("com.github.danbrough.androidutils:logging_android:_")
  implementation("com.github.danbrough.androidutils:menu:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  //implementation("com.sothree.slidinguppanel:library:_")
/*  implementation("com.mikepenz:iconics-core:_")
  implementation("com.mikepenz:iconics-views:_")
  implementation ("com.mikepenz:fontawesome-typeface:_")
  implementation("com.mikepenz:community-material-typeface:_")*/


  implementation(Square.okHttp3.okHttp)


//  implementation(project(":menu"))

  androidTestImplementation(Testing.junit4)

  androidTestImplementation(AndroidX.test.core)
  androidTestImplementation(AndroidX.test.runner)
  androidTestImplementation(AndroidX.test.rules)


/*  testImplementation("ch.qos.logback:logback-classic:_")
  testImplementation("ch.qos.logback:logback-core:_")

  kapt(Google.dagger.hilt.android.compiler)
  implementation(Google.dagger.hilt.android)

  // For instrumentation tests
  androidTestImplementation(Google.dagger.hilt.android.testing)

  // For local unit tests
  testImplementation(Google.dagger.hilt.android.testing)*/


}
