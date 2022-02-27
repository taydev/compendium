package ai.chiyo.compendium.core.element.source

import ai.chiyo.compendium.core.element.Element

abstract class SourcedElement(
    override val elementId: String,
    override var name: String,
    open val sourceId: String,
    open var sourcePage: Int = 0,
    open val fromSrd: Boolean = false
) : Element(elementId, name)
