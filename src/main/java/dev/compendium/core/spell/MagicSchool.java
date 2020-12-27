package dev.compendium.core.spell;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * An object used to represent the various schools of magic throughout the game.
 */
public class MagicSchool {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private String name;

    /**
     * Primary constructor.
     *
     * @param sourceUUID The ID of the source to associate this school with.
     * @param name       The name of the magic school.
     */
    public MagicSchool(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createMagicSchoolUUID(), sourceUUID, name);
    }

    /**
     * Full-args constructor, used primarily for MongoDB element initialisation.
     *
     * @param uuid       The ID of the magic school.
     * @param sourceUUID The ID of the source to associate this magic school with.
     * @param name       The name of the magic school.
     */
    @BsonCreator
    public MagicSchool(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID,
        @BsonProperty("name") String name) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
    }

    /**
     * Returns the unique ID of the magic school, primarily used for database retrieval or identification.
     *
     * @return The ID of the magic school
     */
    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Returns the unique ID of the source the magic school belongs to.
     *
     * @return The ID of the magic school's source.
     */
    public UUID getSourceUUID() {
        return this.sourceUUID;
    }

    /**
     * Returns an object representing the Source that the magic school belongs to.
     *
     * @return The source of the magic school.
     *
     * @see Source
     */
    public Source getSource() {
        return ElementRegistry.getInstance().getSourceByUUID(this.getSourceUUID());
    }

    /**
     * Returns the display name of the magic school.
     *
     * @return The magic school's name.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Updates the display name of the magic school.
     *
     * @param name The new name for the magic school.
     */
    public void setName(String name) {
        this.name = name;
    }
}
