package ai.chiyo.compendium.core.element.data.util

data class Proficiency(
    val proficiency: String,
    var transferOnMulticlass: Boolean = false,
    var optional: Boolean = false
)
