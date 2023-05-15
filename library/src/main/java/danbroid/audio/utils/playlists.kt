package danbroid.audio.utils

import android.content.Context
import danbroid.audio.http.httpSupport
import okhttp3.CacheControl
import java.util.concurrent.TimeUnit

suspend fun parsePlaylistURL(context: Context, url: String): String? =
    context.httpSupport.requestString(url, CacheControl.Builder().maxStale(1, TimeUnit.DAYS).build()).let {
      it.lines().firstNotNullOfOrNull { line ->
        val i = line.indexOf('=');
        if (line.startsWith("File") && i > 0) {
          line.substring(i + 1).trim()
        } else if (line.startsWith("http")) {
          line.trimEnd()
        } else null
      }
    }

