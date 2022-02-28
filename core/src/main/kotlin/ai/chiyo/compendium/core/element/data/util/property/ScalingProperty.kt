package ai.chiyo.compendium.core.element.data.util.property

open class ScalingProperty<T>(open val scales: Map<Int, T> = mutableMapOf()) {
    open fun getHighestScale(level: Int): T? {
        var lastMatchedLevel = -1
        var lastMatchedScale : T? = null
        for (entry in scales.entries) {
            if (entry.key in (lastMatchedLevel + 1)..level) {
                lastMatchedLevel = entry.key
                lastMatchedScale = entry.value
            }
        }
        return lastMatchedScale
    }

    open fun getAllScales(level: Int): List<T> {
        val result = mutableListOf<T>()
        for (entry in scales.entries) {
            if (entry.key <= level) {
                result.add(entry.value)
            }
        }
        return result
    }
}