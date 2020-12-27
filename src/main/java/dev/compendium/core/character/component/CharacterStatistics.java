package dev.compendium.core.character.component;

import java.util.UUID;

public class CharacterStatistics {

    private final UUID uuid;
    private Background background;
    private Race race;
    private int strengthScore;
    private int dexterityScore;
    private int constitutionScore;
    private int intelligenceScore;
    private int wisdomScore;
    private int charismaScore;
    private ExperienceSystem experienceSystem;
    private long experiencePoints;
    private int inspirationCount;

    public CharacterStatistics(UUID uuid) {
        this.uuid = uuid;
    }
}
