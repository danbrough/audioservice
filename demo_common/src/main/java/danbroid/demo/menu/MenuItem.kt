package danbroid.audioservice.app.menu

data class MenuItem(
    val id: String,
    val title: String,
    val subTitle: String,
    val iconURI: Any? = null,
    val isPlayable: Boolean = false,
    val isBrowsable: Boolean = false,
    val onClicked: ((MenuItem) -> Unit)? = null,
) {
  companion object {
    val LOADING_ITEM = MenuItem("", "Loading...", "")
  }
}


private val log = danbroid.logging.getLog(MenuItem::class)

@DslMarker
annotation class MenuDSL


open class MenuBuilder(val builderFactory: () -> MenuBuilder) {
  @MenuDSL
  lateinit var id: String

  @MenuDSL
  lateinit var title: String

  @MenuDSL
  var subtitle: String = ""

  @MenuDSL
  var iconURI: Any? = null

  var children: MutableList<MenuBuilder>? = null

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

  @Suppress("UNCHECKED_CAST")
  fun <T : MenuBuilder> find(id: String): T? {
    //log.dtrace("find() ${this.id} -> $id")
    provides(id)?.also {
      // log.dtrace("returning provider")
      return it as T
    }
    return children?.firstNotNullOfOrNull {
      it.find(id)
    }
  }

  fun buildItem(): MenuItem = MenuItem(
      id, title, subtitle,
      iconURI = iconURI, isBrowsable = isBrowsable, isPlayable = isPlayable,
      onClicked = onClicked
  )

  fun buildChildren(): List<MenuItem> = children?.map { it.buildItem() } ?: emptyList()


  override fun toString() = "MenuBuilder[$id:$title]"

}

@MenuDSL
suspend inline fun <reified T : MenuBuilder> T.menu(child: T = builderFactory.invoke() as T, block: suspend T.() -> Unit) {
  addChild(child)
  child.block()
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
