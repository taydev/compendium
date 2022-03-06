package ai.chiyo.compendium.core.element.data.character

import ai.chiyo.compendium.core.element.source.SourcedElement

data class Race(override val elementId: String, override var name: String, override val sourceId: String, ) : SourcedElement(elementId, name, sourceId)
