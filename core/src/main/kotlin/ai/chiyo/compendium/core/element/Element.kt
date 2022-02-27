package ai.chiyo.compendium.core.element

import java.util.*

abstract class Element(open val elementId: String, open var name: String) {
    constructor(name: String) : this(UUID.randomUUID().toString(), name)
}