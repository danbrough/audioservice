plugins {
  id("de.fayard.refreshVersions") version "0.10.1"
////                          # available:"0.11.0"
}

if (System.getenv("JITPACK") == null) {
  //println("including :demo")
  //include(":demo")
  include(":demo2",":library")
}else {
  println("Not including demo as building on jitpack.io")
}

include(":service")
rootProject.name = "audioservice"

