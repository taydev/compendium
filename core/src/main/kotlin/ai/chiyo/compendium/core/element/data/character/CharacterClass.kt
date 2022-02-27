package ai.chiyo.compendium.core.element.data.character

import ai.chiyo.compendium.core.element.data.character.classdata.AbilityType
import ai.chiyo.compendium.core.element.data.character.classdata.CasterProgression
import ai.chiyo.compendium.core.element.data.leveling.HitDice
import ai.chiyo.compendium.core.element.source.SourcedElement

data class CharacterClass(
    override val elementId: String,
    override var name: String,
    override val sourceId: String,
    override var sourcePage: Int = 0,
    override val fromSrd: Boolean = false,
    val hitDice: HitDice,
    val castingAbility: AbilityType? = null,
    val casterProgression: CasterProgression = CasterProgression.NONE,
    // TODO: finish
    ) : SourcedElement(elementId, name, sourceId)
