package ai.chiyo.compendium.core.element.data.character

import ai.chiyo.compendium.core.element.data.character.classdata.AdditionalSpell
import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.data.util.AbilityType
import ai.chiyo.compendium.core.element.data.util.Choice
import ai.chiyo.compendium.core.element.data.util.Proficiency
import ai.chiyo.compendium.core.element.source.SourcedElement

data class Feat(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    val entries: List<Entry> = mutableListOf(),
    // -- prereqs --
    val prerequisiteRaces: List<String> = mutableListOf(),
    val prerequisiteAbilities: Map<AbilityType, Int> = mutableMapOf(),
    val prerequisiteSpellcasting: Boolean = false,
    val prerequisiteSpellcasters: List<String> = mutableListOf(),
    val prerequisiteFeats: List<String> = mutableListOf(),
    val prerequisiteProficiencies: List<Proficiency> = mutableListOf(),
    val otherPrerequisites: List<String> = mutableListOf(),
    // -- benefits --
    val gainedSpells: List<AdditionalSpell> = mutableListOf(),
    val gainedProficiencies: List<Proficiency> = mutableListOf(),
    val gainedAbilityScore: Map<AbilityType, Int> = mutableMapOf(),
    // other gained stuff ?????? idk how to handle yet, I'll come back to this later
    val choices: Choice = Choice(),
) : SourcedElement(elementId, name, sourceId, sourcePage, fromSrd)
