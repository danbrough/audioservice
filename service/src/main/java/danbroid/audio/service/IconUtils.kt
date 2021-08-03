package danbroid.audio.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.media2.common.MediaMetadata
import coil.Coil
import coil.request.ImageRequest
import danbroid.util.resource.resolveDrawableURI
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class IconUtils(
    val context: Context,
    @ColorInt val iconTint: Int = Config.Notifications.notificationIconTint
) {

  companion object {
    var BITMAP_CONFIG = Bitmap.Config.ARGB_8888
  }

  val callbacks = mutableMapOf<MediaMetadata, (Bitmap) -> Unit>()

  fun loadIcon(
      metadata: MediaMetadata?,
      defaultIcon: Bitmap? = null,
      callback: (Bitmap) -> Unit
  ): Bitmap? {
    log.dtrace("loadIcon() $metadata")

    metadata ?: let {
      log.error("metadata is null")
      return defaultIcon
    }

    if (callbacks.containsKey(metadata)) {
      log.derror("CALLBACK ALREADY SET")
      return null
    }

    callbacks[metadata] = callback

    metadata.getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON)?.also {
      log.trace("found existing display icon bitmap")
      return it
    }

    val imageURI = metadata.getString(MediaMetadata.METADATA_KEY_ART_URI) ?: metadata.getString(
        MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI
    ) ?: run {
      log.trace("no METADATA_KEY_ART_URI or METADATA_KEY_DISPLAY_ICON_URI returning defaultIcon ")
      return defaultIcon
    }

    log.trace("loading icon from imageURI: $imageURI")

    imageURI.resolveDrawableURI(context).also {
      if (it != 0) return drawableToBitmapIcon(it)
    }
    val request = ImageRequest.Builder(context)
        .data(imageURI)
        .size(Config.Notifications.notificationIconWidth)
        .target {
          val drawable = it as BitmapDrawable
          callbacks.remove(metadata)!!.invoke(drawable.bitmap)
        }.build()

    GlobalScope.launch(Dispatchers.IO) {
      Coil.execute(request)
    }
/*
    Glide.with(context).asBitmap().load(imageURI).diskCacheStrategy(DiskCacheStrategy.DATA)
        //.transform(RoundedCorners(iconCornerRadius))
        .into(object : CustomTarget<Bitmap>(
            Config.Notifications.notificationIconWidth,
            Config.Notifications.notificationIconHeight
        ) {
          override fun onLoadCleared(placeholder: Drawable?) = Unit

          override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            log.error("ON RESOURCE READY")
            callbacks.remove(metadata)!!.invoke(resource)

          }
        })
*/



    return defaultIcon
  }

  fun drawableToBitmapIcon(@DrawableRes resID: Int): Bitmap {
    val drawable = ResourcesCompat.getDrawable(context.resources, resID, context.theme)!!

    val bitmap = Bitmap.createBitmap(
        Config.Notifications.notificationIconWidth,
        Config.Notifications.notificationIconHeight,
        BITMAP_CONFIG
    )

    log.dtrace("drawing bitmap ..")
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)

    if (iconTint != 0)
      DrawableCompat.setTint(
          drawable,
          iconTint
      )
    drawable.draw(canvas)
    return bitmap
  }
}

private val log = danbroid.logging.getLog(IconUtils::class)

