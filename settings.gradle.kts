plugins {
  id("de.fayard.refreshVersions") version "0.10.1"
}

if (System.getenv("JITPACK") == null) {
  //println("including :demo")
  //include(":demo")
  include(":demo2",":demo_common")
}else {
  println("Not including demo as building on jitpack.io")
}

include(":service")
rootProject.name = "audioservice"

