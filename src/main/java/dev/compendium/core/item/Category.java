package dev.compendium.core.item;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

/**
 * An object representing the various categories that an Item can belong to.
 *
 * @see Item
 */
public class Category {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<UUID> subcategories;
    private String name;

    /**
     * Primary constructor.
     *
     * @param sourceUUID The ID of the source this category belongs to.
     * @param name       The name of the source.
     */
    public Category(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createCategoryUUID(), sourceUUID, name, new ArrayList<>());
    }

    /**
     * Full-args constructor, used primarily for MongoDB element initialisation.
     *
     * @param uuid          The unique ID of the category object.
     * @param sourceUUID    The unique ID of the source the category belongs to.
     * @param name          The name of the source.
     * @param subcategories The list of child categories for this parent category.
     */
    @BsonCreator
    public Category(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("subcategories") List<UUID> subcategories) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.subcategories = subcategories;
    }

    /**
     * Returns the unique ID associated with the category.
     *
     * @return The ID of the category.
     */
    @BsonId
    public UUID getUUID() {
        return this.uuid;
    }

    /**
     * Returns the unique ID of the source associated with this category.
     *
     * @return The category's source ID.
     */
    public UUID getSourceUUID() {
        return this.sourceUUID;
    }

    /**
     * Returns the source of the category.
     *
     * @return The category's source.
     */
    public Source getSource() {
        return ElementRegistry.getInstance().getSourceByUUID(this.getSourceUUID());
    }

    /**
     * Returns the display name of the category.
     *
     * @return The display name of the category.
     */
    public String getName() {
        return this.name;
    }

    /**
     * Updates the name of the category.
     *
     * @param name The new name of the category.
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Returns a list of the subcategories' IDs belonging to this parent category.
     *
     * @return The list of subcategories' IDs associated with this category.
     */
    public List<UUID> getSubcategoryUUIDs() {
        return this.subcategories;
    }

    /**
     * Returns a list of subcategories associated with this category.
     *
     * @return The list of subcategories for this parent category.
     */
    public List<Category> getSubcategories() {
        List<Category> result = new ArrayList<>();
        for (UUID uuid : this.getSubcategoryUUIDs()) {
            result.add(ElementRegistry.getInstance().getCategoryByUUID(uuid));
        }
        return result;
    }

    /**
     * Adds a category to the parent category's list of subcategories.
     *
     * @param category The category to add to the parent's subcategory list.
     */
    public void addSubcategory(Category category) {
        this.getSubcategoryUUIDs().add(category.getUUID());
    }

    /**
     * Removes the provided category from the parent category's subcategory list.
     *
     * @param category The category to remove from the parent's subcategory list
     */
    public void removeSubcategory(Category category) {
        this.getSubcategoryUUIDs().remove(category.getUUID());
    }
}