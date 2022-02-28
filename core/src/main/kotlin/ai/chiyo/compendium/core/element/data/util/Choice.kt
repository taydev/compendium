package ai.chiyo.compendium.core.element.data.util

data class Choice(var count: Int = 0, val from: List<String> = mutableListOf<String>(), var chosen: Int = 0)
