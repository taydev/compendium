package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Race {

    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;
    // TODO: finish

    public Race(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createRaceUUID(), sourceUUID, name);
    }

    @BsonCreator
    public Race(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID, @BsonProperty("name") String name) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
    }

    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    public UUID getSourceUUID() {
        return this.sourceUUID;
    }

    public Source getSource() {
        return ElementRegistry.getInstance().getSourceByUUID(this.getSourceUUID());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
