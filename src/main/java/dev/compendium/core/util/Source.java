package dev.compendium.core.util;

import dev.compendium.core.ElementRegistry;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * Object used for defining the source or origin of another element, used for identification, filtering, and permissions
 * management.
 */
public class Source {

    @BsonId
    private final UUID uuid;
    @BsonProperty("owner_id")
    private final String ownerID;
    @BsonProperty("discord_ids")
    private final List<String> discordIDs;
    private String name;
    private String abbreviation;
    @BsonProperty("author_name")
    private String authorName;
    @BsonProperty("author_url")
    private String authorUrl;

    /**
     * Primary constructor.
     *
     * @param name       The name of the source.
     * @param authorName The name of the creator of the source.
     * @param discordID  The Discord ID of the creator.
     */
    public Source(String name, String authorName, String discordID) {
        this(ElementRegistry.getInstance().createSourceUUID(), name, ElementUtils.abbreviate(name), authorName,
            authorName, discordID, new ArrayList<>(Collections.singletonList(discordID)));
    }

    /**
     * Full-args constructor, used primarily for MongoDB database element construction.
     *
     * @param uuid         The unique ID of the source.
     * @param name         The name of the source.
     * @param abbreviation The abbreviated identifier of the source, derived from the source name.
     * @param authorName   The name of the creator of the source.
     * @param authorUrl    Optional. The website of the creator of the source.
     * @param ownerID      The Discord ID of the creator.
     * @param discordIDs   Additional Discord IDs that have been added to the source to allow for management.
     */
    @BsonCreator
    public Source(@BsonId UUID uuid, @BsonProperty("name") String name,
        @BsonProperty("abbreviation") String abbreviation, @BsonProperty("author_name") String authorName,
        @BsonProperty("author_url") String authorUrl, @BsonProperty("owner_id") String ownerID,
        @BsonProperty("discord_ids") List<String> discordIDs) {
        this.uuid = uuid;
        this.name = name;
        this.abbreviation = abbreviation;
        this.authorName = authorName;
        this.authorUrl = authorUrl;
        this.ownerID = ownerID;
        this.discordIDs = discordIDs;
    }

    /**
     * Returns the unique ID of the source, primarily for database retrieval or identification.
     *
     * @return The ID of the source.
     */
    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Returns the display name of the source. Used for information purposes or retrieval by name.
     *
     * @return The name of the source.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Updates the display name of the source. This method also updates the abbreviation to reflect the changes.
     *
     * @param name The new name of the source.
     */
    public void setName(String name) {
        this.name = name;
        this.abbreviation = ElementUtils.abbreviate(this.name);
    }

    /**
     * Returns the abbreviation of the name of the source. This is typically a String comprised of the first letter in
     * each word of the source name.
     *
     * @return The source name's abbreviation.
     */
    public String getAbbreviation() {
        return this.abbreviation;
    }

    /**
     * Manually updates the abbreviation of the source's name. This is typically automatically generated at the initial
     * creation of the source, however can be modified using this setter.
     *
     * @param abbreviation The new abbreviation of the source's name.
     */
    public void setAbbreviation(String abbreviation) {
        this.abbreviation = abbreviation;
    }

    /**
     * Returns the name of the author that created the source. Typically a Discord username.
     *
     * @return The source author's name.
     */
    @BsonProperty("author_name")
    public String getAuthorName() {
        return this.authorName;
    }

    /**
     * Updates the name of the author that created the source.
     *
     * @param authorName The new name for the author of the source.
     */
    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    /**
     * Returns the URL of the author that created the source. This is typically a link to a website, and is set to the
     * author's name by default.
     *
     * @return The URL of the author of the source.
     */
    @BsonProperty("author_url")
    public String getAuthorUrl() {
        return this.authorUrl;
    }

    /**
     * Updates the URL of the author of the source.
     *
     * @param authorUrl The new URL for the author of the source
     */
    public void setAuthorUrl(String authorUrl) {
        this.authorUrl = authorUrl;
    }

    /**
     * Returns the Discord ID of the source creator. Used for identification and permissions management. This cannot be
     * changed.
     *
     * @return The Discord ID of the source creator.
     */
    @BsonProperty("owner_id")
    public String getOwnerID() {
        return this.ownerID;
    }

    /**
     * Returns a list of Discord IDs associated with the source. Used for permissions management to determine who can
     * edit or add to the source's elements.
     *
     * @return The associated Discord IDs of the source.
     */
    @BsonProperty("discord_ids")
    public List<String> getDiscordIDs() {
        return this.discordIDs;
    }

    /**
     * Adds a Discord ID to the source's list. Adding an ID this way grants the added ID access to add or remove
     * elements to or from the source.
     *
     * @param discordID The Discord ID to add to the source.
     */
    public void addDiscordID(String discordID) {
        this.getDiscordIDs().add(discordID);
    }

    /**
     * Removes a Discord ID from the source's list. Removing an ID from the list removes the user associated with the
     * ID's ability to modify things within the source.
     *
     * @param discordID The Discord ID to remove from the source.
     */
    public void removeDiscordID(String discordID) {
        this.getDiscordIDs().remove(discordID);
    }

    /**
     * Checks if the provided ID in the parameters is the owner of the source. Used for overall permissions checks.
     *
     * @param discordID The Discord ID to compare to the owner's ID.
     *
     * @return Whether or not the ID provided is equal to that of the source owner's ID.
     */
    public boolean isOwner(String discordID) {
        return this.getOwnerID().equals(discordID);
    }

    /**
     * Checks if the provided ID in the parameters is a member of the source group. Used for content permission checks.
     *
     * @param discordID The Discord ID to compare to the source's list of members.
     *
     * @return Whether or not the ID provided is added in the source's member list.
     */
    public boolean isMember(String discordID) {
        return this.getDiscordIDs().contains(discordID);
    }
}
