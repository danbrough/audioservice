package danbroid.audio.http

import android.content.Context
import danbroid.audio.service.audioServiceConfig
import danbroid.util.misc.SingletonHolder
import io.ktor.client.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File
import java.io.IOException
import kotlin.time.Duration

class HttpSupport2(context: Context) {

  val client by lazy {

    val JsonConfiguration = Json {
      isLenient = false
      ignoreUnknownKeys = true
      allowSpecialFloatingPointValues = true
      useArrayPolymorphism = false
    }

    HttpClient(OkHttp) {
      followRedirects = true
/*
      install(HttpCache) {
        privateStorage = HttpCacheStorage.Unlimited()
      }*/

      engine {
        config {
          cache(context.httpSupport.cache)
        }

      }

      install(JsonFeature) {
        serializer = KotlinxSerializer(JsonConfiguration)
      }

/*      engine {
        endpoint {
          // this: EndpointConfig
          maxConnectionsPerRoute = 100
          pipelineMaxSize = 20
          keepAliveTime = 5000
          connectTimeout = 5000
          connectAttempts = 5
        }
      }*/
    }
  }

  inline suspend fun <reified T : Any> getJson(
    url: String,
    maxStale: Duration? = null,
    maxAge: Duration? = null
  ): T {
    TODO("implement")
  }

  companion object : SingletonHolder<HttpSupport2, Context>(::HttpSupport2)
}

/*
Cache-Control: max-age=<seconds>
Cache-Control: max-stale[=<seconds>]
Cache-Control: min-fresh=<seconds>
Cache-Control: no-cache
Cache-Control: no-store
Cache-Control: no-transform
Cache-Control: only-if-cached
 */


val Context.httpSupport2: HttpSupport2
  get() = HttpSupport2.getInstance(this)

class HttpSupport(context: Context) {

  companion object : SingletonHolder<HttpSupport, Context>(::HttpSupport)

  val cache by lazy {
    Cache(File(context.cacheDir, "okhttp"), context.audioServiceConfig.HTTP_CACHE_SIZE)
  }

  private val okHttp by lazy {
    OkHttpClient.Builder().cache(cache).build()
  }

  @Suppress("BlockingMethodInNonBlockingContext")
  suspend fun requestString(
    url: String,
    cacheControl: CacheControl? = null,
    retryWithoutCache: Boolean = false
  ): String = withContext(Dispatchers.IO) {
    Request.Builder().url(url)
      .also {
        if (cacheControl != null) it.cacheControl(cacheControl)
      }
      .build().let {
        okHttp.newCall(it).execute().use {
          if (it.isSuccessful)
            it.body!!.string()
          else {
            if (cacheControl != null && retryWithoutCache) {
              log.info("$url failed. code:${it.code} message:${it.message} retrying without cache")
              requestString(url)
            } else
              throw IOException("$url failed. code:${it.code} message:${it.message}")
          }
        }
      }
  }
}

val Context.httpSupport: HttpSupport
  get() = HttpSupport.getInstance(this)

private val log = danbroid.logging.getLog(HttpSupport::class)
