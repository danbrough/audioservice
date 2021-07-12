package danbroid.audioservice.app.ui.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import danbroid.audioservice.app.menu.MenuItem
import danbroid.demo.content.AudioTrack
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow

class MenuModel(val menuID: String, context: Context) : ViewModel() {
  init {
    log.dinfo("created menu model for $menuID context:$context")
  }

  private val _children = MutableStateFlow<List<MenuItem>>(emptyList())
  val f: Flow<List<MenuItem>> = flow {

  }


}

class MenuModelFactory(val menuID: String, val context: Context) : ViewModelProvider.NewInstanceFactory() {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = MenuModel(menuID, context) as T
}

@Composable
fun menuModel(menuID: String) = viewModel<MenuModel>(factory = MenuModelFactory(menuID, LocalContext.current))

private val log = danbroid.logging.getLog(MenuModel::class)


fun AudioTrack.toMenuItem(): MenuItem = MenuItem(id, title, subtitle, imageURI)