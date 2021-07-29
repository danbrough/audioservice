package danbroid.audioservice.app.ui.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import danbroid.audioservice.app.menu.MenuBuilder
import danbroid.audioservice.app.menu.MenuItem
import danbroid.demo.content.somaFM
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.*
import kotlin.time.Duration


class MenuModel(val menuID: String, context: Context) : ViewModel() {

  init {
    log.derror("MenuModel() $menuID")
  }

  data class MenuState(val menuItem: MenuItem, val children: List<MenuItem>) {
    companion object {
      val LOADING = MenuState(MenuItem.LOADING_ITEM, emptyList())
    }
  }

  inner class DemoMenuBuilder(val context: Context) : MenuBuilder() {
    fun update(item: MenuItem) {
      log.warn("UPDATING $item")
      _state.value = _state.value.let {
        it.copy(
            children = it.children.toMutableList().map {
              if (it.id == item.id) item else it
            }
        )
      }
    }
  }

  val dynamicTitleFlow = flow {
    var count = 0
    delay(Duration.seconds(1))
    while (true) {
      emit("Title: $count")
      delay(Duration.seconds(1))
      count++
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, "Initial set in model")

  private val somaChannelsFlow = flow {
    emit(context.somaFM.channels())
  }

  val somaFMChannels = somaChannelsFlow.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

  /*
    withContext(Dispatchers.Main) {
      channels.forEach {
        log.warn("CREATING MENU ${it.id}")
        menu {
          id = "somafm://${it.id.uriEncode()}"
          title = it.title
          subTitle = it.description
          icon = it.image
          isPlayable = true
        }
      }
    }*/
  private val _state = MutableStateFlow(MenuState.LOADING)
  val state: StateFlow<MenuState> = _state

/*  init {
    log.dinfo("created menu model for $menuID")

    viewModelScope.launch {
      log.trace("loading menu...$menuID")

      val builder: DemoMenuBuilder?
      val menuBuilder = demoMenu(DemoMenuBuilder(context), context.getString(R.string.app_name))

      withContext(Dispatchers.IO) {
        builder = menuBuilder.find(menuID)
      }

      log.debug("found builder: $builder")

      builder ?: error("Failed to find builder for $menuID")

      val menu: MenuItem = builder.buildItem.invoke()
      val children = builder.buildChildren.invoke()
      _state.value = MenuState(menu, children)
    }
  }*/

  override fun onCleared() {
    log.debug("onCleared() $menuID")
  }
}


class MenuModelFactory(val menuID: String, val context: Context) : ViewModelProvider.NewInstanceFactory() {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>): T = MenuModel(menuID, context) as T
}

@Composable
fun menuModel(menuID: String) = viewModel<MenuModel>(factory = MenuModelFactory(menuID, LocalContext.current))

private val log = danbroid.logging.getLog(MenuModel::class)


