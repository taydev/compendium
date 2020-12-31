package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class ClassFeature {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;
    private String description;
    private int levelRequirement;
    private final Document metadata;

    public ClassFeature(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createClassFeatureUUID(), sourceUUID, name, "", 0, new Document());
    }

    @BsonCreator
    public ClassFeature(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name, @BsonProperty("description") String description,
        @BsonProperty("level_requirement") int levelRequirement, @BsonProperty("metadata") Document metadata) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.levelRequirement = levelRequirement;
        this.metadata = metadata;
    }

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getLevelRequirement() {
        return this.levelRequirement;
    }

    public void setLevelRequirement(int levelRequirement) {
        this.levelRequirement = levelRequirement;
    }

    public Document getMetadata() {
        return this.metadata;
    }
}
