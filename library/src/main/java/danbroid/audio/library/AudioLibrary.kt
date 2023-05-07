package danbroid.audio.library

import androidx.media2.common.MediaItem
import danbroid.audio.menu.Menu
import kotlinx.coroutines.flow.Flow
import kotlinx.serialization.Serializable


@Serializable
sealed class MenuState {
  @Serializable
  data class LOADING(val title: String) : MenuState()

  @Serializable
  data class LOADED(val title: String, val menus: List<Menu>) : MenuState()

  @Serializable
  data class ERROR(val message: String) : MenuState()
}


interface AudioLibrary {
  suspend fun loadItem(mediaID: String): MediaItem?

  fun loadMenus(menuID: String): Flow<MenuState>? = null
}


object RootAudioLibrary : AudioLibrary {

  private val libraries = mutableListOf<AudioLibrary>()

  fun register(vararg libs: AudioLibrary) {
    libraries.addAll(libs)
  }

  override suspend fun loadItem(mediaID: String): MediaItem? =
      libraries.firstNotNullOfOrNull {
        it.loadItem(mediaID)
      }

  override fun loadMenus(menuID: String): Flow<MenuState>? =
      libraries.firstNotNullOfOrNull {
        it.loadMenus(menuID)
      }


}