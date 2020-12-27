package dev.compendium.core.character.component;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

public class CharacterAppearance implements Serializable {

    private final UUID uuid;
    private String playerName;
    private String characterName;
    private UUID alignmentUUID;
    private long age;
    private String height;
    private String weight;
    private String eyes;
    private String skin;
    private String hair;
    private String backstory;
    private String additionalInfo;
    private List<String> personalityTraits;
    private List<String> ideals;
    private List<String> bonds;
    private List<String> flaws;

    public CharacterAppearance(UUID uuid) {
        this.uuid = uuid;
    }
}
