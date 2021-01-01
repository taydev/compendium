package dev.compendium.core.item;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.ElementUtils;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Currency {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;
    private String abbreviation;
    private float goldPieceEquivalent;

    public Currency(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createCurrencyUUID(), sourceUUID, name, ElementUtils.abbreviate(name), 1);
    }

    public Currency(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name, String abbreviation, float goldPieceEquivalent) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.abbreviation = abbreviation;
        this.goldPieceEquivalent = goldPieceEquivalent;
    }

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
        this.abbreviation = ElementUtils.abbreviate(this.getName());
    }

    public String getAbbreviation() {
        return this.abbreviation;
    }

    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    public float getGoldPieceEquivalent() {
        return this.goldPieceEquivalent;
    }

    public void setGoldPieceEquivalent(float goldPieceEquivalent) {
        this.goldPieceEquivalent = goldPieceEquivalent;
    }
}
