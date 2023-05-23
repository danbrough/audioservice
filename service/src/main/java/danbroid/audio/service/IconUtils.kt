package danbroid.audio.service

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import androidx.annotation.ColorInt
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.media3.common.MediaMetadata

class IconUtils(
  val context: Context,
  @ColorInt val iconTint: Int = Config.Notifications.notificationIconTint
) {


  suspend fun loadIcon(metadata: MediaMetadata): BitmapDrawable? {
    TODO("implement")

    /*    log.trace("loadIcon() $metadata")

        val imageURI = metadata.getString(MediaMetadata.METADATA_KEY_ART_URI)
          ?: metadata.getString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI)


        *//*
        imageURI.resolveDrawableURI(context).also {
          if (it != 0) return drawableToBitmapIcon(it)
        }
    *//*

    log.trace("loading image $imageURI...")
    val request = ImageRequest.Builder(context)
      .data(imageURI)
      .size(Config.Notifications.notificationIconWidth)
      .allowHardware(false)
      .fallback(R.drawable.ic_audio_track)
      .build()


    log.trace("making request for $imageURI")
    val result = Coil.execute(request)
    log.trace("got result: $result")
    return result.drawable as? BitmapDrawable*/
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


  fun drawableToBitmapIcon(@DrawableRes resID: Int): BitmapDrawable {
    val drawable =
      ResourcesCompat.getDrawable(context.resources, resID, context.theme)!! as BitmapDrawable

    val bitmap = Bitmap.createBitmap(
      Config.Notifications.notificationIconWidth,
      Config.Notifications.notificationIconHeight,
      Bitmap.Config.ARGB_8888
    )

    log.trace("drawing bitmap ..")
    val canvas = Canvas(bitmap)
    drawable.setBounds(0, 0, canvas.width, canvas.height)

    if (iconTint != 0)
      DrawableCompat.setTint(
        drawable,
        iconTint
      )
    drawable.draw(canvas)
    return drawable
  }
}


