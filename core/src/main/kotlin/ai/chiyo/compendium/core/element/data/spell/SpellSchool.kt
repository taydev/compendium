package ai.chiyo.compendium.core.element.data.spell

import ai.chiyo.compendium.core.element.source.SourcedElement
import java.util.*

data class SpellSchool(
    override val elementId: String,
    override var name: String,
    override var sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    ) : SourcedElement(elementId, name, sourceId) {
        constructor(name: String, sourceId: String) : this(UUID.randomUUID().toString(), name, sourceId)
    }
