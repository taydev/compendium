package dev.compendium.core.util;

// TODO: does this even need documentation?
public enum TimeUnit {
    INSTANTANEOUS,
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
        if (duration < 1) {
            return "Instantaneous";
        }
        return duration + " " + this.name().replaceAll("_", " ").toLowerCase() + (duration > 1 ? "s" : "");
    }

    public static TimeUnit getTime(String input) {
        input = input.toLowerCase().replaceAll("[^a-z]", "");
        if (input.equalsIgnoreCase("instantaneous")) {
            return INSTANTANEOUS;
        } else if (input.contains("reaction")) {
            return REACTION;
        } else if (input.contains("bonus action")) {
            return BONUS_ACTION;
        } else if (input.contains("action")) {
            return ACTION;
        } else if (input.contains("second")) {
            return SECOND;
        } else if (input.contains("minute")) {
            return MINUTE;
        } else if (input.contains("hour")) {
            return HOUR;
        } else if (input.contains("day")) {
            return DAY;
        } else if (input.contains("week")) {
            return WEEK;
        } else if (input.contains("month")) {
            return MONTH;
        } else if (input.contains("year")) {
            return YEAR;
        } else {
            return INSTANTANEOUS;
        }
    }
}
