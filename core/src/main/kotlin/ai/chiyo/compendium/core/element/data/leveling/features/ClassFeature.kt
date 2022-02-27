package ai.chiyo.compendium.core.element.data.leveling.features

import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.data.leveling.Feature

data class ClassFeature(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var fromSrd: Boolean = false,
    override var sourcePage: Int = 0,
    override var shortName: String = name,
    override var isVariant: Boolean = false,
    override var variantId: String? = null,
    override val entries: List<Entry> = mutableListOf<Entry>(),
    var classId: String = "",
) : Feature(elementId, name, sourceId,  fromSrd, sourcePage, shortName, isVariant, variantId, entries)
