package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.ElementUtils;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: write documentation
public class Alignment {

    @BsonId
    private final UUID uuid;
    @BsonProperty("source_uuid")
    private final UUID sourceUUID;
    @BsonProperty("name")
    private String name;
    @BsonProperty("abbreviation")
    private String abbreviation;

    public Alignment(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Alignment(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createAlignmentUUID(), sourceUUID, name,
            ElementUtils.abbreviate(name));
    }

    @BsonCreator
    public Alignment(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("abbreviation") String abbreviation) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.abbreviation = abbreviation;
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

    @BsonProperty("name")
    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @BsonProperty("abbreviation")
    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
