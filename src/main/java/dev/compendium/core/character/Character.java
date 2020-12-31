package dev.compendium.core.character;

import dev.compendium.core.character.component.CharacterClass;
import dev.compendium.core.item.Item;
import java.util.List;
import java.util.Map;
import java.util.UUID;

// TODO: finish
public class Character {

    private final UUID uuid;
    private List<Map<CharacterClass, Integer>> levels;
    private int maximumHitPoints;
    private int currentHitPoints;
    private int temporaryHitPoints;
    private List<UUID> inventory;
    private Item equippedArmour;
    private Item itemInMainHand;
    private Item itemInOffHand;

    public Character() {
        this.uuid = UUID.randomUUID();
    }
}
