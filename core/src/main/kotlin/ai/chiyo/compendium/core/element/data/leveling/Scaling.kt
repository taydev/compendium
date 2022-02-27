package ai.chiyo.compendium.core.element.data.leveling

data class Scaling(var scales: Map<Int, String>) {
    constructor() : this(mutableMapOf<Int, String>())

    fun getHighestScale(level: Int): String {
        var lastMatchedLevel = -1
        var lastMatchedScale = ""
        for (entry in scales.entries) {
            if (entry.key in (lastMatchedLevel + 1)..level) {
                lastMatchedLevel = entry.key
                lastMatchedScale = entry.value
            }
        }
        return lastMatchedScale
    }

    fun getAllScales(level: Int): List<String> {
        val result = mutableListOf<String>()
        for (entry in scales.entries) {
            if (entry.key <= level) {
                result.add(entry.value)
            }
        }
        return result
    }
}