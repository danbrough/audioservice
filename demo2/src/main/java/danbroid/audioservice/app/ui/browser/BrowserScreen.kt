package danbroid.audioservice.app.ui.browser

import android.annotation.SuppressLint
import android.content.Context
import android.webkit.JavascriptInterface
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedCallback
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.accompanist.insets.statusBarsHeight
import danbroid.audioservice.app.DemoAudioClientModel
import danbroid.audioservice.app.R
import danbroid.audioservice.app.rnz.RNZLibrary


private fun createWebView(context: Context, audioClientModel: DemoAudioClientModel): WebView {
  val webView = WebView(context)

  // val cssRegex = "https://www.rnz.co.nz/x/application.*\\.css".toRegex().toPattern()
  val jsRegex = "https://www.rnz.co.nz/x/application.*\\.js".toRegex().toPattern()
//  val audioRegex = "https://www\\.rnz\\.co\\.nz.*/\\d\\d\\d\\d\\d\\d\\d\\d\\d.*".toRegex().toPattern()

  val onBackPressedCallback = object : OnBackPressedCallback(false) {
    override fun handleOnBackPressed() {
      log.dinfo("handleOnBackPressed()")
      webView.goBack()
    }
  }

  webView.webViewClient = object : WebViewClient() {
    override fun onPageFinished(view: WebView, url: String) {
      log.dwarn("onPageFinished() $url canGoBack: ${view.canGoBack()}")
      onBackPressedCallback.isEnabled = view.canGoBack()
    }

    @Suppress("DEPRECATION")
    override fun shouldInterceptRequest(view: WebView, url: String): WebResourceResponse? {
/*
      if (cssRegex.matcher(url).matches()) {
        return WebResourceResponse(
            "text/css",
            "UTF-8",
            context!!.resources.openRawResource(R.raw.rnzcss)
        )
      }
*/

      if (jsRegex.matcher(url).matches()) {
        log.trace("returning jss: $url")
        return WebResourceResponse(
            "text/javascript",
            "UTF-8",
            context.resources.openRawResource(R.raw.rnz)
        )
      }
      return null
    }
  }

  webView.settings.apply {
    javaScriptCanOpenWindowsAutomatically = false
    javaScriptEnabled = true
    setSupportZoom(true)
    builtInZoomControls = true
    displayZoomControls = false
  }

  val activity = context as ComponentActivity
  activity.onBackPressedDispatcher.addCallback(activity, onBackPressedCallback)

  class Console {
    /**
     * Instantiate the interface and set the context
     */

    @JavascriptInterface
    fun warn(o: String) {
      log.dwarn(o)
    }

    /**
     * Show a toast from the web page
     */
    @JavascriptInterface
    fun showToast(toast: String) {
      Toast.makeText(activity, toast, Toast.LENGTH_SHORT).show()
    }

    @JavascriptInterface
    fun debug(o: String) {
      log.ddebug(o)
    }

    @JavascriptInterface
    fun info(o: String) {
      log.dinfo(o)
    }

    @JavascriptInterface
    fun mediaLink(data: String) {
      log.dinfo("mediaLink() data:$data")
      activity.runOnUiThread {
        //activity.activityModel().playMedia("RNZ:${data.substring(data.indexOf('X') + 1)}")
        val code = data.substring(data.indexOf('X') + 1)
        //log.trace("playing $code")
        // (activity as ActivityInterface).open("$URI_RNZ_PROGRAMME_PREFIX/$code")
      }
    }

    @JavascriptInterface
    fun onClick(href: String?, data: String, clazz: String?) {
      log.dtrace("onClick() href:$href data:$data clazz:$clazz")

      val i = data.indexOf('X')
      if (i < 0) return

      val progID = data.substringAfter('X', "0").toLong()
      if (progID == 0L) return

      activity.runOnUiThread {
        audioClientModel.play(RNZLibrary.getProgrammeURI(progID))
        //TODO
        /* val progID = java.lang.Long.parseLong(data.substring(data.indexOf('X') + 1))
         MediaControllerCompat.getMediaController(activity).transportControls
           .playFromMediaId("$RNZ_ID_PREFIX$progID", null)*/
      }
    }

    @JavascriptInterface
    fun overLinkLongClicked(href: String) {
      log.ddebug(" overLinkLongClicked():$href")
    }
  }

  webView.addJavascriptInterface(Console(), "audienz")

  return webView
}


@SuppressLint("SetJavaScriptEnabled")
@Composable
fun BrowserScreen(audioClientModel: DemoAudioClientModel) {
  log.dtrace("BrowserScreen()")
  Column {
    Spacer(Modifier.fillMaxWidth().statusBarsHeight().background(MaterialTheme.colors.primary))
    AndroidView(factory = { createWebView(it, audioClientModel) }, modifier = Modifier.fillMaxWidth()) {
      log.dtrace("BrowserScreen update")
      it.loadUrl("https://rnz.co.nz")
    }
  }
}


private val log = danbroid.logging.getLog("danbroid.audioservice.app.ui.browser")
