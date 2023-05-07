plugins {
  id("de.fayard.refreshVersions") version "0.51.0"
}

if (System.getenv("JITPACK") == null) {
  //println("including :demo")
  //include(":demo")
 // include(":demo2")
//  include(":audienz")

//  project(":audienz").projectDir = file("../audienz/app")

} else {
  println("Not including demo as building on jitpack.io")
}

include(":library", ":service")
rootProject.name = "audioservice"

