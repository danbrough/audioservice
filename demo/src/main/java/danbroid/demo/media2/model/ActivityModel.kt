package danbroid.demo.media2.model

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import kotlinx.coroutines.flow.MutableStateFlow

class ActivityModel(context: Context) : ViewModel() {
}

fun ComponentActivity.activityModel(): ActivityModel =
    ViewModelProvider(this,
        object : ViewModelProvider.NewInstanceFactory() {
          @Suppress("UNCHECKED_CAST")
          override fun <T : ViewModel?> create(modelClass: Class<T>): T = ActivityModel(this@activityModel) as T
        }
    ).get()


private val log = danbroid.logging.getLog(ActivityModel::class)
