package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Proficiency {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final Metadata metadata;
    private String name;

    public Proficiency(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createProficiencyUUID(), sourceUUID, name, new Metadata());
    }

    @BsonCreator
    public Proficiency(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name, @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.metadata = metadata;
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

    public Metadata getMetadata() {
        return this.metadata;
    }
}
