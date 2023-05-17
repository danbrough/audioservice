plugins {
  id("com.android.application")
  kotlin("android")
  kotlin("kapt")
  //kotlin("serialization")
  kotlin("plugin.serialization")

}



android {

  compileSdk = ProjectVersions.SDK_VERSION

  namespace = "danbroid.audioservice.app"

  defaultConfig {
    vectorDrawables.useSupportLibrary = true

    minSdk = ProjectVersions.MIN_SDK_VERSION
    targetSdk = ProjectVersions.SDK_VERSION
    versionCode = ProjectVersions.BUILD_VERSION
    versionName = ProjectVersions.VERSION_NAME
    multiDexEnabled = true
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    //consumerProguardFiles("consumer-rules.pro")

    vectorDrawables {
      useSupportLibrary = true
    }
  }

  signingConfigs {
    register("release") {
      storeFile = file("/home/dan/.android/busapp_keystore2")
      keyAlias = "wellybusapp"
      storePassword = project.property("KEYSTORE_PASSWORD")?.toString() ?: ""
      keyPassword = project.property("KEYSTORE_PASSWORD")?.toString() ?: ""
    }
  }

  buildTypes {

    getByName("debug") {
      //debuggable(true)
    }

    getByName("release") {
      isMinifyEnabled = ProjectVersions.MINIFY_ENABLED
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro"
      )
      signingConfig = signingConfigs.getByName("release")
    }
  }

  lint {
    //isAbortOnError = false

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
      "androidx.compose.foundation.ExperimentalFoundationApi",
      "kotlin.time.ExperimentalTime",

      //"kotlinx.coroutines.ExperimentalCoroutinesApi",
      //"kotlinx.coroutines.FlowPreview",
      //"androidx.compose.material.ExperimentalMaterialApi"
    ).forEach {

      languageSettings.optIn(it)
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
  implementation(project(":library"))
  //implementation(project(":demo_common"))

  implementation(AndroidX.lifecycle.runtime.ktx)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.lifecycle.viewModelKtx)
  //implementation(AndroidX.coreKtx)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)
  implementation(AndroidX.media2.common)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")
  implementation(AndroidX.core.ktx)
  implementation(KotlinX.serialization.json)
  implementation(KotlinX.serialization.cbor)
  implementation("org.danbrough:klog:_")

  //implementation(KotlinX.serialization.cbor)
  //implementation(KotlinX.serialization.protobuf)

  implementation("com.github.fornewid:material-motion-compose:_")


  //implementation(AndroidX.navigation.commonKtx)
  implementation(AndroidX.navigation.compose)
  implementation(AndroidX.activity.compose)
  implementation(AndroidX.lifecycle.viewModelCompose)
  implementation(AndroidX.constraintLayout.compose)

  implementation(AndroidX.compose.ui)
  implementation(AndroidX.compose.ui.text)
  implementation(AndroidX.compose.ui.util)
  implementation(AndroidX.compose.ui.viewBinding)
  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.compose.runtime.liveData)
  implementation(AndroidX.compose.material)
  implementation(AndroidX.compose.material.icons.extended)
  implementation(AndroidX.compose.ui.tooling)


  implementation(Google.accompanist.systemUiController)
  implementation(Google.accompanist.insets)
  //implementation("com.google.accompanist:accompanist-coil:_")
  implementation(COIL.compose)

  //implementation(AndroidX.media2.exoplayer)
  //implementation(AndroidX.media2.player)
  //implementation("com.google.guava:guava:_")

  //implementation("com.github.danbrough.androidutils:logging_android:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  implementation("com.github.danbrough.androidutils:compose:_")


  implementation(Square.okHttp3.okHttp)

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
