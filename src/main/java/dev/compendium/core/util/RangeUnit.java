package dev.compendium.core.util;

// TODO: does this even need documentation either?
public enum RangeUnit {
    TOUCH,
    SELF,
    FOOT,
    MILE;

    public static RangeUnit getRange(String rangeLabel) {
        if (rangeLabel.equalsIgnoreCase("foot") || rangeLabel.equalsIgnoreCase("feet")) {
            return FOOT;
        } else if (rangeLabel.toLowerCase().contains("mile")) {
            return MILE;
        } else if (rangeLabel.toLowerCase().contains("self")) {
            return SELF;
        } else {
            return TOUCH;
        }
    }

    public String getName(int range) {
        String name = this.name().toLowerCase();
        if (range != 1) {
            if (this == FOOT) {
                name = "feet";
            } else if (this == MILE) {
                name = "miles";
            }
        }

        return range + " " + name;
    }
}
