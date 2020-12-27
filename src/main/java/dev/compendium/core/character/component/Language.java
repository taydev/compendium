package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Language {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;
    private String description;

    public Language(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createLanguageUUID(), sourceUUID, name, "");
    }

    @BsonCreator
    public Language(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("description") String description) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
