package danbroid.audioservice.app.ui.menu

import android.content.Context
import androidx.compose.runtime.compositionLocalOf
import androidx.navigation.NavHostController
import danbroid.audio.library.AudioClientViewModel
import danbroid.audio.menu.MenuModel

class MenuContext(
    val id: String,
    val context: Context,
    val menuModel: MenuModel,
    val audioClientModel: AudioClientViewModel,
    val navHostController: NavHostController) {


  companion object {
    var NEXT_ID = 0
  }


}

val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.menu")

val LocalMenuContext = compositionLocalOf<MenuContext> { error("No local provided for LocalMenuContext") }
