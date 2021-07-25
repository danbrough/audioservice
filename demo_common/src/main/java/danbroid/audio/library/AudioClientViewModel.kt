package danbroid.audio.library

import android.content.Context
import android.content.Intent
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.common.util.concurrent.ListenableFuture
import danbroid.audio.client.AudioClient
import danbroid.audio.service.AudioService

open class AudioClientViewModel(context: Context) : ViewModel() {

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


  private val mainExecutor = ContextCompat.getMainExecutor(context)

  protected fun <T> ListenableFuture<T>.then(job: (T) -> Unit) =
      addListener({
        job.invoke(get())
      }, mainExecutor)

  companion object {
    class AudioClientViewModelFactory(val context: Context) : ViewModelProvider.NewInstanceFactory() {
      override fun <T : ViewModel?> create(modelClass: Class<T>): T = modelClass.getDeclaredConstructor(Context::class.java).newInstance(context) as T
    }
  }
}

private val log = danbroid.logging.getLog(AudioClientViewModel::class)
