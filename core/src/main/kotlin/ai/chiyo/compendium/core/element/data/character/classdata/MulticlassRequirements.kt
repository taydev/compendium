package ai.chiyo.compendium.core.element.data.character.classdata

import ai.chiyo.compendium.core.element.data.util.AbilityType

data class MulticlassRequirements(
    val scores: Map<AbilityType, Int> = mutableMapOf(),
    var requireAll: Boolean = true
)
