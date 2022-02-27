package ai.chiyo.compendium.core.element.data.entry.types

import ai.chiyo.compendium.core.element.data.entry.Entry

data class TextEntry(var _name: String, var content: String) : Entry(_name)