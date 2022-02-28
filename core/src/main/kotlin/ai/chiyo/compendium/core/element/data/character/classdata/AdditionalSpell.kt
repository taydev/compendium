package ai.chiyo.compendium.core.element.data.character.classdata

data class AdditionalSpell(
    var name: String = "",
    var type: AdditionalSpellType,
    var level: Int,
    var spell: String,
    var ritual: Boolean
)
