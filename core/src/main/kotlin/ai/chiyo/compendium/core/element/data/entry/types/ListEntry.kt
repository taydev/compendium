package ai.chiyo.compendium.core.element.data.entry.types

import ai.chiyo.compendium.core.element.data.entry.Entry

data class ListEntry(override var name: String, var content: List<String>) : Entry(name)
