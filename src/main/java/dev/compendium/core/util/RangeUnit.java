package dev.compendium.core.util;

// TODO: does this even need documentation either?
public enum RangeUnit {
    TOUCH,
    FOOT,
    MILE;

    public String getName(int range) {
        if (range != 1) {
            if (this == FOOT) {
                return "feet";
            } else if (this == MILE) {
                return "miles";
            }
        }

        return this.name().toLowerCase();
    }
}
