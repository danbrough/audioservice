plugins {
  id("com.android.library")
  id("kotlin-android")
  id("maven-publish")
  kotlin("plugin.serialization")
}

android {
  compileSdk = ProjectVersions.SDK_VERSION
  namespace = "danbroid.audio.library"

  defaultConfig {
    minSdk = ProjectVersions.MIN_SDK_VERSION
    testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    consumerProguardFiles("consumer-rules.pro")
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
      "androidx.compose.material.ExperimentalMaterialApi",
    ).forEach {
      languageSettings.optIn(it)
    }
  }


}



afterEvaluate {

  val sourcesJar by tasks.registering(Jar::class) {
    archiveClassifier.set("sources")
    from(android.sourceSets.getByName("main").java.srcDirs)
  }

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

dependencies {
  implementation(project(":service"))
  implementation(AndroidX.core.ktx)
  implementation(AndroidX.media2.common)
  implementation(KotlinX.serialization.json)
  implementation("com.github.danbrough.androidutils:misc:_")
  implementation("com.github.danbrough.androidutils:logging_core:_")
  compileOnly(AndroidX.compose.runtime)
  implementation(Square.okHttp3.okHttp)
  implementation("org.jetbrains.kotlin:kotlin-reflect:_")
  implementation(AndroidX.lifecycle.viewModelKtx)
  implementation(AndroidX.compose.runtime)
  implementation(AndroidX.constraintLayout.compose)
  implementation(AndroidX.compose.material)
  implementation(AndroidX.compose.material.icons.extended)

  implementation(Google.android.material)
  api(AndroidX.media2.common)
  api(AndroidX.media2.session)

  implementation(AndroidX.navigation.compose)
  implementation(AndroidX.activity.compose)
  implementation(AndroidX.lifecycle.viewModelCompose)

}