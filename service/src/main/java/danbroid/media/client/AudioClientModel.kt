package danbroid.media.client

import android.content.Context
import android.content.Intent
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import danbroid.media.service.AudioService
import kotlin.time.ExperimentalTime

@ExperimentalTime
class AudioClientModel(context: Context) : ViewModel() {


  private val _client = lazy {
    log.derror("starting audio service ..")

    context.startService(Intent(context, AudioService::class.java))
    AudioClient(context)
  }


  val client: AudioClient by _client


  override fun onCleared() {
    super.onCleared()
    log.info("onCleared()")
    if (_client.isInitialized()) {
      log.debug("closing client..")
      _client.value.close()
    }
  }
}

class AudioClientModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
  @Suppress("UNCHECKED_CAST")
  override fun <T : ViewModel?> create(modelClass: Class<T>) = AudioClientModel(context) as T
}

fun Fragment.audioClientModel(): AudioClientModel = activityViewModels<AudioClientModel> {
  AudioClientModelFactory(requireContext())
}.value


fun ComponentActivity.audioClientModel(): AudioClientModel = viewModels<AudioClientModel> {
  AudioClientModelFactory(this)
}.value


private val log = danbroid.logging.getLog(AudioClientModel::class)