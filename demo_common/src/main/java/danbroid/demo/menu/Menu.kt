package danbroid.demo.menu

import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable

@Serializable
data class Menu(
    var id: String,
    var title: String,
    var subTitle: String = "",
    @Contextual
    var icon: Any? = null,
    var onClicked: (() -> Unit)? = null,
    var isBrowsable: Boolean = false,
    var isPlayable: Boolean = false
)

