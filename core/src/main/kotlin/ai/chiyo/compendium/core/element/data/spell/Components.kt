package ai.chiyo.compendium.core.element.data.spell

data class Components(
    var verbal: Boolean,
    var somatic: Boolean,
    var material: Boolean,
    var materialContents: String?,
    var materialCost: Int?
)