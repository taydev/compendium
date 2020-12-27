package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import java.util.UUID;

public class CharacterClass {

    private final UUID uuid;
    private String name;

    public CharacterClass(String name) {
        this.uuid = ElementRegistry.getInstance().createClassUUID();
        this.name = name;
    }

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
