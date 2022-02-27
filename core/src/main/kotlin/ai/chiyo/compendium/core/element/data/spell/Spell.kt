package ai.chiyo.compendium.core.element.data.spell

import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.data.level.Scaling
import ai.chiyo.compendium.core.element.data.mapping.*
import ai.chiyo.compendium.core.element.data.time.Duration
import ai.chiyo.compendium.core.element.data.time.TimeData
import ai.chiyo.compendium.core.element.source.SourcedElement

data class Spell(
    // base data
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    // spell metadata
    val sourcePage: Int,
    val srd: Boolean,
    var spellLevel: Int,
    var school: SpellSchool,
    var castTime: TimeData,
    var components: Components,
    var duration: Duration,
    var isRitual: Boolean,
    val entries: List<Entry>,
    val higherLevel: List<Entry>,
    // damage data
    var scaling: Scaling,
    val savingThrows: List<String>,
    var abilityChecks: List<String>,
    val damageInflict: List<String>,
    val conditionInflict: List<String>,
    val affectsCreatureTypes: List<String>,
    val damageResist: List<String>,
    val damageImmune: List<String>,
    val damageVulnerable: List<String>,
    // linked data
    val fromClasses: List<FromClass>,
    val fromClassVariants: List<FromClassVariant>,
    val fromSubclasses: List<FromSubclass>,
    val fromEldritchInvocations: List<FromEldritchInvocation>,
    val fromBackgrounds: List<FromBackground>,
    val fromRaces: List<FromRace>
) : SourcedElement(elementId, name, sourceId)
