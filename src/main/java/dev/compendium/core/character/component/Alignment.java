package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.ElementUtils;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: write documentation
public class Alignment {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;
    private String abbreviation;

    public Alignment(Source source, String name) {
        this(ElementRegistry.getInstance().createAlignmentUUID(), source.getUUID(), name,
            ElementUtils.abbreviate(name));
    }

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

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }
}
