package danbroid.audio.menu

import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient
import kotlin.reflect.KProperty

@DslMarker
annotation class MenuDSL


@Serializable
data class Menu(
    var id: String,
    var title: String,
    var subTitle: String = "",
    var iconUrl: String? = null,
    var onClicked: (() -> Unit)? = null,
    var isBrowsable: Boolean = false,
    var isPlayable: Boolean = false,
    var isHidden: Boolean = false
) {
  @Transient
  private var iconData: Any? = null

  @Transient
  var icon: Any? by object {
    operator fun getValue(menu: Menu, property: KProperty<*>): Any? {
      return iconData ?: iconUrl
    }

    operator fun setValue(menu: Menu, property: KProperty<*>, value: Any?) {
      when (value) {
        is String -> iconUrl = value
        else -> iconData = value
      }
    }
  }

}




