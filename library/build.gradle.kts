plugins {
  id("com.android.library")
  kotlin("android")
  `maven-publish`
  kotlin("plugin.serialization")
}

android {
  compileSdk = ProjectVersions.SDK_VERSION
  //buildToolsVersion = ProjectVersions.BUILD_TOOLS_VERSION
  namespace = "danbroid.audio.library"

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    //targetSdk = ProjectVersions.SDK_VERSION
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
  }
  compileOptions {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
  }
  kotlinOptions {
    jvmTarget = "17"
  }


  buildFeatures {
    viewBinding = true
    compose = true
  }

  buildTypes {
    release {
      isMinifyEnabled = false
      proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
    }
  }

/*
  compileOptions {
    sourceCompatibility = ProjectVersions.JAVA_VERSION
    targetCompatibility = ProjectVersions.JAVA_VERSION
  }

  kotlinOptions {
    jvmTarget = ProjectVersions.KOTLIN_JVM_VERSION
  }
*/

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
      languageSettings.optIn(it)
    }
  }

  val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(sourceSets.getByName("main").java.srcDirs)
  }

  afterEvaluate {
    publishing {
      val projectName = name
      publications {
        val release by registering(MavenPublication::class) {
          /*components.forEach {
        println("Publication component: ${it.name}")
      }*/
          from(components["release"])
          artifact(sourcesJar.get())
          artifactId = projectName
          groupId = ProjectVersions.GROUP_ID
          version = ProjectVersions.VERSION_NAME
        }
      }
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
  implementation(AndroidX.compose.runtime)
  implementation(Square.okHttp3.okHttp)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")

  implementation(AndroidX.lifecycle.viewModelKtx)
  implementation(AndroidX.compose.runtime)
  implementation("androidx.constraintlayout:constraintlayout-compose:_")
  implementation(AndroidX.compose.material)
  implementation(AndroidX.compose.material.icons.extended)
  implementation(Ktor.client.core)
  implementation(Ktor.client.okHttp)
  implementation(Ktor.client.cio)
  implementation(Ktor.client.contentNegotiation)


  implementation("io.ktor:ktor-serialization-kotlinx-json:_")

  implementation(Ktor.client.logging)
  implementation(Ktor.client.serialization)
  implementation(Google.android.material)
  api("androidx.media2:media2-common:_")
  api("androidx.media2:media2-session:_")

  implementation("androidx.navigation:navigation-compose:_")
  implementation("androidx.activity:activity-compose:_")
  implementation("androidx.lifecycle:lifecycle-viewmodel-compose:_")

}