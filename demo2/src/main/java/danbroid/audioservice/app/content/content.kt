package danbroid.audioservice.app.content

import danbroid.audioservice.app.R
import danbroid.audioservice.app.Routes
import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuBuilderContext
import danbroid.demo.content.testTracks

internal val log = danbroid.logging.getLog("danbroid.audioservice.app.content")

const val URI_PREFIX = "audiodemo:/"
const val URI_CONTENT = "$URI_PREFIX/content"

const val URI_SETTINGS = "$URI_PREFIX/settings"

suspend fun MenuBuilderContext.demoMenu(rootTitle: String): MenuBuilder = MenuBuilder(this).apply {
  id = URI_CONTENT
  title = rootTitle

  menu {
    title = context.getString(R.string.test)
    subtitle = "Menu with Children"

    menu {
      title = "Child 1"
      subtitle = "First Child"
    }


    menu {
      title = "Child 2"
      subtitle = "Second Child"
    }

    menu {
      id = Routes.SETTINGS
      title = "Navigate by ROUTE.SETTINGS to Settings"
    }
  }

  menu {
    id = URI_SETTINGS
    title = context.getString(R.string.settings)
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