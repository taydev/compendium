package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Feat {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final Metadata metadata;
    private String name;
    private String description;

    public Feat(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Feat(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createFeatUUID(), sourceUUID, name, "", new Metadata());
    }

    @BsonCreator
    public Feat(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("description") String description, @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.metadata = metadata;
    }

    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    public UUID getSourceUUID() {
        return this.sourceUUID;
    }

    @BsonIgnore
    public Source getSource() {
        return ElementRegistry.getInstance().getSourceByUUID(this.getSourceUUID());
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }
}
