package ai.chiyo.compendium.core.util

object ElementUtils {
    fun getAbbreviationFromName(name: String): String {
        return buildString {
            for (word in name.split(" ")) {
                append(word[0])
            }
        }
    }
}