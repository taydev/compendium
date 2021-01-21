package dev.compendium.core.util;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Creator {

    @BsonId
    private final String discordId;
    @BsonProperty("has_read_terms")
    private boolean readTerms;

    public Creator(String discordId) {
        this(discordId, false);
    }

    @BsonCreator
    public Creator(@BsonId String discordId, @BsonProperty("has_read_terms") boolean readTerms) {
        this.discordId = discordId;
        this.readTerms = readTerms;
    }

    @BsonId
    public String getDiscordId() {
        return this.discordId;
    }

    @BsonProperty("has_read_terms")
    public boolean isReadTerms() {
        return this.readTerms;
    }

    public void setReadTerms(boolean readTerms) {
        this.readTerms = readTerms;
    }
}
