package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Language {

    @BsonId
    private final UUID uuid;
    @BsonProperty("source_uuid")
    private final UUID sourceUUID;
    private String name;
    private String description;
    @BsonProperty("language_type")
    private LanguageType languageType;

    public Language(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Language(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createLanguageUUID(), sourceUUID, name, "", LanguageType.STANDARD);
    }

    @BsonCreator
    public Language(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("description") String description, @BsonProperty("language_type") LanguageType languageType) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.languageType = languageType;
    }

    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    @BsonProperty("source_uuid")
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

    @BsonProperty("language_type")
    public LanguageType getLanguageType() {
        return this.languageType;
    }

    public void setLanguageType(LanguageType languageType) {
        this.languageType = languageType;
    }
}
