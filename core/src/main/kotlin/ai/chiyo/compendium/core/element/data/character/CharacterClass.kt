package ai.chiyo.compendium.core.element.data.character

import ai.chiyo.compendium.core.element.data.character.classdata.*
import ai.chiyo.compendium.core.element.data.character.classdata.CasterProgression
import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.data.entry.types.TableEntry
import ai.chiyo.compendium.core.element.data.leveling.HitDice
import ai.chiyo.compendium.core.element.data.util.*
import ai.chiyo.compendium.core.element.data.util.property.NamedScalingProperty
import ai.chiyo.compendium.core.element.data.util.property.ScalingProperty
import ai.chiyo.compendium.core.element.source.SourcedElement

data class CharacterClass(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    var hitDice: HitDice,
    var sidekick: Boolean = false,
    val entries: List<Entry> = mutableListOf(),
    val castingAbility: AbilityType? = null,
    val casterProgression: CasterProgression = CasterProgression.NONE,
    // variable names suck
    val spellsKnownProgression: ScalingProperty<Int> = ScalingProperty(),
    val spellsKnownProgressionFixed: ScalingProperty<Int> = ScalingProperty(),
    val spellsKnownProgressionFixedByLevel: ScalingProperty<Map<Int, Int>> = ScalingProperty(),
    val spellsKnownProgressionFixedAllowLowerLevel: Boolean = false,
    val cantripsKnownProgression: ScalingProperty<Int> = ScalingProperty(),
    // TODO: create variable syntax for custom elements
    //        - maybe just inherit from 5e.tools? <$var$>
    //        - dunno
    var preparedSpells: String = "0",
    val additionalSpells: List<AdditionalSpell> = mutableListOf(),
    val startingSavingThrowProficiencies: List<Proficiency> = mutableListOf(),
    val startingArmorProficiencies: List<Proficiency> = mutableListOf(),
    val startingWeaponProficiencies: List<Proficiency> = mutableListOf(),
    val startingToolProficiencies: List<Proficiency> = mutableListOf(),
    val startingSkillProficiencies: Choice = Choice(),
    val startingEquipment: List<Choice> = mutableListOf(),
    var startingEquipmentGoldEquivalent: String = "",
    val multiclassRequirements: MulticlassRequirements = MulticlassRequirements(),
    val classTable: TableEntry = TableEntry("$name - Class Features"),
    val classFeatures: Map<Int, List<String>> = mutableMapOf(),
    val featureProgressions: Map<String, ScalingProperty<Int>> = mutableMapOf(),
    var subclassTitle: String = "$name Subclass"
    // finished...?
    ) : SourcedElement(elementId, name, sourceId)
