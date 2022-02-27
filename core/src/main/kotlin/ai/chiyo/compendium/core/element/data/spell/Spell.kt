package ai.chiyo.compendium.core.element.data.spell

import ai.chiyo.compendium.core.element.data.entry.Entry
import ai.chiyo.compendium.core.element.data.leveling.Scaling
import ai.chiyo.compendium.core.element.data.mapping.*
import ai.chiyo.compendium.core.element.data.time.Duration
import ai.chiyo.compendium.core.element.data.time.DurationType
import ai.chiyo.compendium.core.element.data.time.TimeData
import ai.chiyo.compendium.core.element.data.time.TimeUnit
import ai.chiyo.compendium.core.element.source.SourcedElement
import java.util.*

data class Spell(
    // base data
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    // spell metadata
    var spellLevel: Int = 0,
    var school: String = "",
    var castTime: TimeData = TimeData(
        duration = 1,
        unit = TimeUnit.ACTION
    ),
    var components: Components = Components(
        verbal = false,
        somatic = false,
        material = false,
        materialContents = null,
        materialCost = null
    ),
    var duration: Duration = Duration(
        type = DurationType.INSTANT,
        duration = 0,
        timeUnit = TimeUnit.INSTANT,
        concentration = false
    ),
    var isRitual: Boolean = false,
    val entries: List<Entry> = mutableListOf<Entry>(),
    val higherLevel: List<Entry> = mutableListOf<Entry>(),
    // damage data
    var scaling: Scaling = Scaling(),
    val savingThrows: List<String> = mutableListOf<String>(),
    var abilityChecks: List<String> = mutableListOf<String>(),
    val damageInflict: List<String> = mutableListOf<String>(),
    val conditionInflict: List<String> = mutableListOf<String>(),
    val affectsCreatureTypes: List<String> = mutableListOf<String>(),
    val damageResist: List<String> = mutableListOf<String>(),
    val damageImmune: List<String> = mutableListOf<String>(),
    val damageVulnerable: List<String> = mutableListOf<String>(),
    // linked data
    val fromClasses: List<FromClass> = mutableListOf<FromClass>(),
    val fromClassVariants: List<FromClassVariant> = mutableListOf<FromClassVariant>(),
    val fromSubclasses: List<FromSubclass> = mutableListOf<FromSubclass>(),
    val fromEldritchInvocations: List<FromEldritchInvocation> = mutableListOf<FromEldritchInvocation>(),
    val fromBackgrounds: List<FromBackground> = mutableListOf<FromBackground>(),
    val fromRaces: List<FromRace> = mutableListOf<FromRace>()
) : SourcedElement(elementId, name, sourceId) {
    constructor(name: String, sourceId: String) : this(UUID.randomUUID().toString(), name, sourceId)
}
