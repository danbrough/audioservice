plugins {
  `kotlin-dsl`
}

/*
dependencies {
  implementation("org.jetbrains.kotlin:kotlin-stdlib:1.4.0")
}
*/


repositories {
  mavenCentral()
}

kotlin {
  jvmToolchain {
    languageVersion.set(JavaLanguageVersion.of(11))
  }
}
java {
  toolchain.languageVersion.set(JavaLanguageVersion.of(11))
}
