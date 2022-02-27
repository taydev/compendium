package ai.chiyo.compendium.core.element.source

import ai.chiyo.compendium.core.element.Element

abstract class SourcedElement(
    override val elementId: String,
    override var name: String,
    open val sourceId: String
) : Element(elementId, name)
