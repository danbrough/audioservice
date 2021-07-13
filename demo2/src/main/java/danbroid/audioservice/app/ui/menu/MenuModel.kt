package danbroid.audioservice.app.ui.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import danbroid.audioservice.app.R
import danbroid.audioservice.app.content.demoMenu
import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuBuilderContext
import danbroid.audioservice.app.menu.MenuItem
import danbroid.demo.content.AudioTrack
import danbroid.util.misc.SingletonHolder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class MenuModel(val menuID: String, context: Context) : ViewModel() {

  private val getString: (Int) -> String = { context.getString(it) }

  val menuBuilder = MenuContent.getInstance(context)

  companion object MenuContent : SingletonHolder<MenuBuilder, Context>({
    val ctx = BuilderContext(it)
    log.derror("Created builder context")
    runBlocking(Dispatchers.IO) {
      ctx.demoMenu(ctx.getString(R.string.app_name))
    }
  })

  class BuilderContext(val context: Context) : MenuBuilderContext() {
    override fun getString(id: Int): String = context.getString(id)
  }

  private val _children = MutableStateFlow<List<MenuItem>>(emptyList())
  val children: StateFlow<List<MenuItem>> = _children

  suspend fun test() {
    log.info("test()")


    log.debug("created menuBuilder: $menuBuilder")
    menuBuilder.children?.forEach {
      log.debug("found child $it")
    }

    menuBuilder.buildChildren().also {
      _children.value = it
    }

  }

  init {
    log.dinfo("created menu model for $menuID context:$context")
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