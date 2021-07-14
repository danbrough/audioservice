package danbroid.audioservice.app.menu

import android.content.Context
import androidx.annotation.StringRes

data class MenuItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val imageURI: String? = null,
    val playable: Boolean = false,
    val browsable: Boolean = false) {
  companion object {
    val LOADING_ITEM = MenuItem("", "Loading...", "")
  }
}


private val log = danbroid.logging.getLog(MenuItem::class)

@DslMarker
annotation class MenuDSL


open class MenuBuilderContext(val context: Context) {
  open fun getString(@StringRes id: Int) = context.getString(id)
}

class MenuBuilder(val context: MenuBuilderContext) {
  @MenuDSL
  lateinit var id: String

  @MenuDSL
  lateinit var title: String

  @MenuDSL
  var subtitle: String = ""

  @MenuDSL
  var iconURI: String? = null

  var children: MutableList<MenuBuilder>? = null

  var icon: (() -> Unit)? = null

  @MenuDSL
  var isBrowsable = false
    get() = field || !children.isNullOrEmpty()

  @MenuDSL
  var isPlayable: Boolean = false

  @MenuDSL
  var provides: ((String) -> MenuBuilder?) = {
    if (it == id) this else null
  }


  @MenuDSL
  var onClicked: ((MenuItem) -> Unit)? = null

  fun addChild(child: MenuBuilder) {
    // log.error("addChild() $id -> child: ${child.id}")
    if (!child::id.isInitialized)
      child.id = if (id.endsWith('/')) "$id${children?.size ?: 0}" else "${id}/${children?.size ?: 0}"

    isBrowsable = true
    val childBuilders = children ?: mutableListOf<MenuBuilder>().also {
      children = it
    }
    childBuilders.add(child)
  }

/*  fun traverse(): Sequence<MenuBuilder> = sequence {
    yield(this@MenuBuilder)
    children?.forEach {
      yieldAll(it.traverse())
    }
  }*/

  fun find(id: String): MenuBuilder? {
    log.dtrace("find() ${this.id} -> $id")
    if (this.id == id) return this
    provides(id)?.also {
      log.dtrace("returning provider")
      return it
    }
    return children?.firstNotNullOfOrNull {
      it.find(id)
    }
  }

  fun buildItem(): MenuItem = MenuItem(
      id, title, subtitle,
      imageURI = iconURI, browsable = isBrowsable, playable = isPlayable
  )

  fun buildChildren(): List<MenuItem> = children?.map { it.buildItem() } ?: emptyList()

  @MenuDSL
  suspend fun menu(child: MenuBuilder = MenuBuilder(context), block: suspend MenuBuilder.() -> Unit) {
    addChild(child)
    child.block()
  }

  override fun toString() = "MenuBuilder[$id:$title]"

}


/*
@MenuDSL
inline fun MenuBuilder T.menu(
    child: T = T::class.java.getConstructor(Context::class.java).newInstance(requireContext()),
    block: T.() -> Unit
): T {
  child.id = if (id.endsWith('/')) "$id${children?.size ?: 0}" else "${id}/${children?.size ?: 0}"
  addChild(child)
  child.block()
  return child
}

*/
