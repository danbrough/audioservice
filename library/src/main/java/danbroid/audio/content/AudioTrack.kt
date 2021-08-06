package danbroid.audio.content

import android.graphics.Bitmap
import androidx.core.os.bundleOf
import androidx.media2.common.MediaMetadata
import danbroid.audio.menu.Menu

@DslMarker
annotation class TestDataDSL

typealias TestData = MutableList<Menu>


@TestDataDSL
fun testData(block: TestData.() -> Unit) = mutableListOf<Menu>().apply {
  block()
}

@TestDataDSL
fun TestData.menu(block: Menu.() -> Unit) = Menu("", "").also {
  it.block()
  it.isPlayable = true
  add(it)
}


fun MediaMetadata.toMenu(): Menu =
    Menu(
        getString(MediaMetadata.METADATA_KEY_MEDIA_ID)!!,
        getText(MediaMetadata.METADATA_KEY_DISPLAY_TITLE)?.toString() ?: "Untitled",
        getText(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE)?.toString() ?: "",
        iconUrl = getText(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI).toString(),
    ).also {
      it.icon = getBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON)
    }


fun Menu.toMediaMetadata(): MediaMetadata.Builder = MediaMetadata.Builder()
    .putString(MediaMetadata.METADATA_KEY_MEDIA_ID, id)
    .putString(MediaMetadata.METADATA_KEY_MEDIA_URI, id)
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_TITLE, title)
    .putString(MediaMetadata.METADATA_KEY_DISPLAY_SUBTITLE, subTitle)
    .putLong(MediaMetadata.METADATA_KEY_PLAYABLE, 1)
    .putString(MediaMetadata.METADATA_KEY_ARTIST, subTitle)
    .setExtras(bundleOf())

    .also { builder ->
      val iconData = icon
      if (iconUrl != null) builder.putString(MediaMetadata.METADATA_KEY_DISPLAY_ICON_URI, iconUrl)
      if (iconData is Bitmap)
        builder.putBitmap(MediaMetadata.METADATA_KEY_DISPLAY_ICON, iconData)
    }



