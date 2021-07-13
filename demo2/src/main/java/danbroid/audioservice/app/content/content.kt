package danbroid.audioservice.app.content

import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuBuilderContext
import danbroid.demo.common.R
import danbroid.demo.content.testTracks

//internal val log = danbroid.logging.getLog("danbroid.audioservice.app.content")

const val CONTENT_PREFIX = "demo:/"
const val CONTENT_ROOT = "$CONTENT_PREFIX/content"


suspend fun MenuBuilderContext.demoMenu(rootTitle: String): MenuBuilder = MenuBuilder(this).apply {
  id = CONTENT_ROOT
  title = rootTitle

  menu {
    title = getString(R.string.test)
    subtitle = "Subtitle 1"
  }

  menu {
    title = "Menu 2"
    subtitle = "Subtitle 2"
  }

  testTracks.testData.forEach { audioTrack ->
    menu {
      id = audioTrack.id
      title = audioTrack.title
      subtitle = audioTrack.subtitle
      isPlayable = true
    }
  }
}