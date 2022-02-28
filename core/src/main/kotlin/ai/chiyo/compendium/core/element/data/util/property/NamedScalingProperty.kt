package ai.chiyo.compendium.core.element.data.util.property

data class NamedScalingProperty<T>(
    var name: String = "",
    override val scales: Map<Int, T> = mutableMapOf()
) : ScalingProperty<T>(scales)
