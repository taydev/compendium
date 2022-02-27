package ai.chiyo.compendium.core.element.data.spell

import ai.chiyo.compendium.core.element.source.SourcedElement

data class SpellSchool(
    override val elementId: String, override var name: String,
    override var sourceId: String
) : SourcedElement(elementId, name, sourceId)
