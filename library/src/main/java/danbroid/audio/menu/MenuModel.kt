package danbroid.audio.menu

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import danbroid.audio.content.somaFM
import danbroid.audio.log
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.stateIn
import kotlin.time.DurationUnit
import kotlin.time.toDuration


open class MenuModel(val menuID: String, context: Context) : ViewModel() {

  init {
    log.debug("MenuModel() $menuID")
  }

  val dynamicTitleFlow = flow {
    var count = 0
    delay(1.toDuration(DurationUnit.SECONDS))
    while (true) {
      emit("Title: $count")
      delay(1.toDuration(DurationUnit.SECONDS))
      count++
    }
  }.stateIn(viewModelScope, SharingStarted.Lazily, "Initial set in model")

  val somaChannels = flow {
    emit(context.somaFM.channels())
  }.stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

  override fun onCleared() {
    log.debug("onCleared() $menuID")
  }
}


class MenuModelFactory(val menuID: String, val context: Context) :
  ViewModelProvider.NewInstanceFactory() {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel> create(modelClass: Class<T>): T = MenuModel(menuID, context) as T
}


@Composable
inline fun <reified T : MenuModel> menuModel(menuID: String) = viewModel<T>(
  factory = MenuModelFactory(menuID, LocalContext.current),
  key = menuID
)




