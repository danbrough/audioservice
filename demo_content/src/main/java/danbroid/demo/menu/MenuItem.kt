package danbroid.audioservice.app.menu

data class MenuItem(val id: String, val title: String, val subTitle: String, val imageURI: String? = null) {
}

//private val log = danbroid.logging.getLog(MenuItem::class)
@DslMarker
annotation class MenuDSL


class MenuBuilderContext() {

}

class MenuBuilder(val context: MenuBuilderContext) {
  @MenuDSL
  var id: String? = null

  var children: MutableList<MenuBuilder>? = null

  @MenuDSL
  var isBrowsable = false
    get() = field || !children.isNullOrEmpty()


  @MenuDSL
  var provides: ((String) -> MenuBuilder?) = {
    if (it == id) this else null
  }

  fun addChild(child: MenuBuilder) {
    // log.error("addChild() $id -> child: ${child.id}")
    child.id = if (id?.endsWith('/') == true) "$id${children?.size ?: 0}" else "${id}/${children?.size ?: 0}"

    isBrowsable = true
    val childBuilders = children ?: mutableListOf<MenuBuilder>().also {
      children = it
    }
    childBuilders.add(child)
  }

  fun traverse(): Sequence<MenuBuilder> = sequence {
    yield(this@MenuBuilder)
    children?.forEach {
      yieldAll(it.traverse())
    }
  }

  fun find(id: String): MenuBuilder? {
    if (this.id == id) return this
    provides(id)?.also { return it }
    return children?.firstNotNullOfOrNull {
      it.find(id)
    }
  }

  @MenuDSL
  suspend fun menu(child: MenuBuilder = MenuBuilder(context), block: suspend MenuBuilder.() -> Unit) {

  }
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
