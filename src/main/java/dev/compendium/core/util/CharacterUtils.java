package dev.compendium.core.util;

public class CharacterUtils {

    public static int calculateProficiencyBonus(int level) {
        return (int) (1 + Math.ceil(level / 4.0));
    }

}
