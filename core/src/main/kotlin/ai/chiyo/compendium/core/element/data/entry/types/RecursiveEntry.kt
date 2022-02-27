package ai.chiyo.compendium.core.element.data.entry.types

import ai.chiyo.compendium.core.element.data.entry.Entry

data class RecursiveEntry(override var name: String, var content: List<Entry>) : Entry(name)