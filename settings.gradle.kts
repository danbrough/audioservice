plugins {
  id("de.fayard.refreshVersions") version "0.10.1"
}

if (System.getenv("JITPACK") == null) {
  //println("including :demo")
  include(":demo",":demo2")
}else {
  println("Not including demo as building on jitpack.io")
}

include(":service")
rootProject.name = "audioservice"

