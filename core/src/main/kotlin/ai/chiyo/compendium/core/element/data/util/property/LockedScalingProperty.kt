package ai.chiyo.compendium.core.element.data.util.property

data class LockedScalingProperty<T>(
    override val scales: Map<Int, T> = mutableMapOf(),
    var requires: String = ""
) : ScalingProperty<T>(scales)