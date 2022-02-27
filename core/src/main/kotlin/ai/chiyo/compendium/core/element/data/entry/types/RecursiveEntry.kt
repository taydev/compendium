package ai.chiyo.compendium.core.element.data.entry.types

import ai.chiyo.compendium.core.element.data.entry.Entry

data class RecursiveEntry(var _name: String, var content: List<Entry>) : Entry(_name)