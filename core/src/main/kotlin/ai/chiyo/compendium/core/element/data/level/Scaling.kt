package ai.chiyo.compendium.core.element.data.level

data class Scaling(var scales: Map<Int, String>) {
    fun getScale(level: Int): String {
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
}