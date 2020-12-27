package dev.compendium.core.util;

// TODO: does this even need documentation?
public enum TimeUnit {
    ACTION,
    BONUS_ACTION,
    REACTION,
    SECOND,
    MINUTE,
    HOUR,
    DAY,
    WEEK,
    MONTH,
    YEAR;

    public String getName(int duration) {
        return this.name().replaceAll("_", " ").toLowerCase() + (duration != 1 ? "s" : "");
    }
}
