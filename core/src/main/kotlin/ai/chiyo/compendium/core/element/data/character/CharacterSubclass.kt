package ai.chiyo.compendium.core.element.data.character

import ai.chiyo.compendium.core.element.data.character.classdata.AdditionalSpell
import ai.chiyo.compendium.core.element.data.character.classdata.CasterProgression
import ai.chiyo.compendium.core.element.data.entry.types.TableEntry
import ai.chiyo.compendium.core.element.data.util.AbilityType
import ai.chiyo.compendium.core.element.data.util.property.ScalingProperty
import ai.chiyo.compendium.core.element.source.SourcedElement

data class CharacterSubclass(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    var shortName: String = name,
    var classId: String,
    val castingAbility: AbilityType? = null,
    val casterProgression: CasterProgression = CasterProgression.NONE,
    val additionalSpells: List<AdditionalSpell> = mutableListOf(),
    val spellsKnownProgression: ScalingProperty<Int> = ScalingProperty(),
    val cantripsKnownProgression: ScalingProperty<Int> = ScalingProperty(),
    val subclassFeatures: Map<Int, List<String>> = mutableMapOf(),
    var gainAtNextFeatureLevel: Boolean = false,
    val subclassTable: TableEntry = TableEntry("$name - Table"),
) : SourcedElement(elementId, name, sourceId)
