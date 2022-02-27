package ai.chiyo.compendium.core.element.data.leveling

import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.source.SourcedElement

abstract class Feature(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var fromSrd: Boolean = false,
    override var sourcePage: Int = 0,
    open var shortName: String = name,
    open var isVariant: Boolean = false,
    open var variantId: String? = null,
    open val entries: List<Entry> = mutableListOf<Entry>()
    ) : SourcedElement(elementId, name, sourceId)
