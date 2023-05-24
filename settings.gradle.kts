plugins {
  id("de.fayard.refreshVersions") version "0.51.0"
}


include(":demo")
include(":demo2")


include(":library", ":service")
rootProject.name = "audioservice"

