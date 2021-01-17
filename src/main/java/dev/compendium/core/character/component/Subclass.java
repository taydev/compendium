package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Subclass {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<UUID> subclassFeatureUUIDs;
    private String name;
    private String description;

    public Subclass(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Subclass(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createSubclassUUID(), sourceUUID, name, "", new ArrayList<>());
    }

    @BsonCreator
    public Subclass(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name, @BsonProperty("description") String description,
        @BsonProperty("subclass_feature_uuids") List<UUID> subclassFeatureUUIDs) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.subclassFeatureUUIDs = subclassFeatureUUIDs;
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

    public List<UUID> getSubclassFeatureUUIDs() {
        return this.subclassFeatureUUIDs;
    }

    @BsonIgnore
    public List<Feature> getSubclassFeatures() {
        List<Feature> result = new ArrayList<>();
        for (UUID uuid : this.getSubclassFeatureUUIDs()) {
            result.add(ElementRegistry.getInstance().getFeatureByUUID(uuid));
        }
        return result;
    }

    public void addSubclassFeature(Feature feature) {
        this.addSubclassFeature(feature.getUUID());
    }

    public void addSubclassFeature(UUID uuid) {
        this.getSubclassFeatureUUIDs().add(uuid);
    }

    public void removeSubclassFeature(Feature feature) {
        this.removeSubclassFeature(feature.getUUID());
    }

    public void removeSubclassFeature(UUID uuid) {
        this.getSubclassFeatureUUIDs().remove(uuid);
    }
}
