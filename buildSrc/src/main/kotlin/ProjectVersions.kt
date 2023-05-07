import org.gradle.api.JavaVersion
import java.util.*


object ProjectVersions {
  var SDK_VERSION = 30
  var MIN_SDK_VERSION = 21
  const val JAVA_VERSION = 11
  const val KOTLIN_VERSION = "11"
  var BUILD_TOOLS_VERSION = "31.0.0"
  var BUILD_VERSION = 1
  var VERSION_OFFSET = 1
  var GROUP_ID = "com.github.danbrough.audioservice"
  var KEYSTORE_PASSWORD = ""
  var VERSION_FORMAT = ""
  val NDK_VERSION = "21.3.6528147"
  const val COMPOSE_VERSION = "1.1.0-alpha01"
  const val COMPOSE_TOOLS_VERSION = "1.1.0-alpha01"
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
