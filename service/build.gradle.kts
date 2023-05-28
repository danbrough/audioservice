plugins {
  id("com.android.library")
  kotlin("android")
  id("maven-publish")
  id("kotlin-parcelize")
  kotlin("kapt")
}

android {


  compileSdk = ProjectVersions.SDK_VERSION

  namespace = "danbroid.audio"

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
    addManifestPlaceholders(mapOf("projectVersion" to ProjectVersions.getVersionName()))
  }

  buildFeatures {
    //aidl = true
  }

  buildTypes {

    getByName("release") {
      isMinifyEnabled = false
      proguardFiles(
        getDefaultProguardFile("proguard-android-optimize.txt"),
        "proguard-rules.pro"
      )
    }

  }

  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }

  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_VERSION
  }

  kotlin.sourceSets.all {
    setOf(
      //"kotlinx.serialization.ExperimentalSerializationApi",
      //"androidx.compose.material.ExperimentalMaterialApi",
      //"androidx.compose.animation.ExperimentalAnimationApi",
      "kotlin.time.ExperimentalTime",
      "androidx.media3.common.util.UnstableApi",
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
    /*  outputs.upToDateWhen {
        false
      }*/
  }
}


dependencies {

  // implementation(AndroidX.coreKtx)
  implementation(Kotlin.stdlib.jdk8)
  implementation(KotlinX.coroutines.android)
  implementation(AndroidX.annotation)
  implementation(AndroidX.lifecycle.liveDataKtx)
  implementation(AndroidX.core.ktx)

  //implementation(AndroidX.fragment.ktx)
  //api(AndroidX.media2.common)
  //api(AndroidX.media2.session)

  implementation("org.jetbrains.kotlin:kotlin-reflect:_")

  //implementation(AndroidX.media2.session)
//  implementation(project(":session"))


  //implementation(AndroidX.media2.player)
  //implementation(project(":exomedia2"))


  //api(AndroidX.concurrent.futures)

  api("com.google.guava:guava:_")
  implementation("org.danbrough:klog:_")

  //api(AndroidX.media2.exoplayer)
//  implementation(Google.android.material)
  // implementation("com.google.guava:guava:_")
  implementation("com.github.danbrough.androidutils:misc:_")
  /*  api("com.github.bumptech.glide:glide:_")
    kapt("com.github.bumptech.glide:compiler:_")*/

  implementation(AndroidX.palette)
  implementation(COIL)

  /*
    val exo_vanilla = false
    val exo_package =
        if (exo_vanilla) "com.google.android.exoplayer" else "com.github.danbrough.exoplayer"
    if (exo_vanilla) {
      implementation("$exo_package:exoplayer-core:$exo_version")
      implementation("$exo_package:exoplayer-smoothstreaming:$exo_version")
      implementation("$exo_package:exoplayer-ui:$exo_version")
      implementation("$exo_package:exoplayer-hls:$exo_version")
      implementation("$exo_package:exoplayer-dash:$exo_version")

      implementation("$exo_package:exoplayer-media2:$exo_version")

      implementation("$exo_package:extension-cast:$exo_version")
    } else {*/


  /*
    implementation("com.github.danbrough.exoplayer:exoplayer-core:_")
    implementation("com.github.danbrough.exoplayer:exoplayer-smoothstreaming:_")
    implementation("com.github.danbrough.exoplayer:exoplayer-ui:_")
    implementation("com.github.danbrough.exoplayer:exoplayer-hls:_")
    implementation("com.github.danbrough.exoplayer:exoplayer-dash:_")
    implementation("com.github.danbrough.exoplayer:extension-flac:_")
    implementation("com.github.danbrough.exoplayer:extension-opus:_")

    implementation("com.github.danbrough.exoplayer:extension-media2:_")
  */

  implementation(AndroidX.media3.session)
  implementation(AndroidX.media3.exoPlayer)
  implementation(AndroidX.media3.dataSource)
  implementation(AndroidX.media3.cast)
  implementation(AndroidX.media3.ui)
  /*
          api "androidx.media3:media3-exoplayer:_"
        api "androidx.media3:media3-datasource:_"
        api "androidx.media3:media3-ui:_"
        api "androidx.media3:media3-session:_"
        api "androidx.media3:media3-cast:_"
   */
  //implementation("androidx.media3:media3-session:_")

  //implementation("com.github.danbrough.exoplayer:extension-opus:_")
  //implementation("com.github.danbrough.exoplayer:extension-cast:_")
  //implementation("com.github.danbrough.exoplayer:extension-opus:_")
  // implementation("com.github.danbrough.exoplayer:extension-flac:_")

  implementation(Square.okHttp3.okHttp)


  //implementation("com.github.danbrough.exoplayer:extension-media2:_")


  //implementation(AndroidX.appCompat)
  testImplementation(Testing.junit4)
  androidTestImplementation(AndroidX.test.ext.junit)
  androidTestImplementation(AndroidX.test.espresso.core)
}




afterEvaluate {
  val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
  }

  publishing {
    val projectName = name
    publications {
      register<MavenPublication>("release") {
        from(components["release"])
        artifact(sourcesJar.get())
        artifactId = projectName
        groupId = ProjectVersions.GROUP_ID
        version = ProjectVersions.VERSION_NAME
      }
    }
  }
}

/*
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            release(MavenPublication) {
                // Applies the component for the release build variant.
                from components.release

                // You can then customize attributes of the publication as shown below.
                groupId = 'com.example.MyLibrary'
                artifactId = 'final'
                version = '1.0'
            }
            // Creates a Maven publication called “debug”.
            debug(MavenPublication) {
                // Applies the component for the debug build variant.
                from components.debug

                groupId = 'com.example.MyLibrary'
                artifactId = 'final-debug'
                version = '1.0'
            }
        }
    }

 */
