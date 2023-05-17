plugins {
  `kotlin-dsl`
}

/*
dependencies {
  implementation(Kotlin.stdlib)
}
*/


repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(17))
  }
}
java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}
