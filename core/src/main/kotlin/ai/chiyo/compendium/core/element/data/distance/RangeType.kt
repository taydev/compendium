package ai.chiyo.compendium.core.element.data.distance

enum class RangeType {
    TOUCH {
        override fun getDisplayString(distance: Int): String {
            return "Touch"
        }
    },
    FEET {
        override fun getDisplayString(distance: Int): String {
            return buildString {
                append(distance)
                if (distance != 1) {
                    append(" feet")
                } else {
                    append(" foot")
                }
            }
        }
    },
    MILES {
        override fun getDisplayString(distance: Int): String {
            return buildString {
                append(distance)
                if (distance != 1) {
                    append(" miles")
                } else {
                    append(" mile")
                }
            }
        }
    },
    SIGHT {
        override fun getDisplayString(distance: Int): String {
            return "Sight"
        }
    };

    abstract fun getDisplayString(distance: Int): String
}