package ai.chiyo.compendium.core.element.source

import ai.chiyo.compendium.core.element.Element
import ai.chiyo.compendium.core.util.ElementUtils
import java.util.*

data class Source(
    override val elementId: String,
    override var name: String,
    var abbreviation: String
) : Element(elementId, name) {
    constructor(elementId: String, name: String) : this(
        elementId,
        name,
        ElementUtils.getAbbreviationFromName(name)
    )

    constructor(name: String) : this(
        UUID.randomUUID().toString(),
        name,
        ElementUtils.getAbbreviationFromName(name)
    )

    fun updateAbbreviation() {
        this.abbreviation = ElementUtils.getAbbreviationFromName(this.name)
    }
}