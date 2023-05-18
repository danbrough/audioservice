import org.gradle.api.JavaVersion
import java.util.Properties


object ProjectVersions {
  var SDK_VERSION = 33
  var MIN_SDK_VERSION = 21
  val JAVA_VERSION = JavaVersion.VERSION_17
  const val KOTLIN_VERSION = "17"

  //var BUILD_TOOLS_VERSION = "31.0.0"
  var BUILD_VERSION = 1
  var VERSION_OFFSET = 1
  var GROUP_ID = "org.danbrough.audioservice"
  var KEYSTORE_PASSWORD = ""
  var VERSION_FORMAT = ""

  //val NDK_VERSION = "21.3.6528147"
  const val COMPOSE_VERSION = "1.4.3"

  const val MINIFY_ENABLED = true
  const val LOCAL_AUDIOSERVICE = true
  const val URI_SCHEME = "audiodemo"
  val VERSION_NAME: String
    get() = getVersionName()

  fun init(props: Properties) {
    BUILD_VERSION = props.getProperty("buildVersion", "1").toInt()
    VERSION_OFFSET = props.getProperty("versionOffset", "1").toInt()
    VERSION_FORMAT = props.getProperty("versionFormat", "0.0.%d")
  }

  fun getIncrementedVersionName() = getVersionName(BUILD_VERSION + 1)


  fun getVersionName(version: Int = BUILD_VERSION) =
    VERSION_FORMAT.format(version - VERSION_OFFSET)
}
