package dev.compendium.core.item;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: finish
public class Item {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<String> additionalTags;
    private final Document metadata;
    private String name;
    private UUID categoryUUID;
    private int cost;
    private CurrencyUnit currencyUnit;
    private int weight;
    private String description;
    private boolean attunement;
    private String attunementRequirements;

    public Item(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createItemUUID(), sourceUUID, name, (null), 0, (null), 0, new ArrayList<>(),
            "", false, "", new Document());
    }

    public Item(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("category_uuid") UUID categoryUUID, @BsonProperty("cost") int cost,
        @BsonProperty("currency_unit") CurrencyUnit currencyUnit, @BsonProperty("weight") int weight,
        @BsonProperty("additional_tags") List<String> additionalTags, @BsonProperty("description") String description,
        @BsonProperty("attunement") boolean attunement,
        @BsonProperty("attunement_requirements") String attunementRequirements,
        @BsonProperty("metadata") Document metadata) {
        this.uuid = uuid;
        this.name = name;
        this.sourceUUID = sourceUUID;
        this.categoryUUID = categoryUUID;
        this.cost = cost;
        this.currencyUnit = currencyUnit;
        this.weight = weight;
        this.additionalTags = additionalTags;
        this.description = description;
        this.attunement = attunement;
        this.attunementRequirements = attunementRequirements;
        this.metadata = metadata;
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

    public UUID getCategoryUUID() {
        return this.categoryUUID;
    }

    public void setCategoryUUID(UUID categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    public Category getCategory() {
        return ElementRegistry.getInstance().getCategoryByUUID(this.getCategoryUUID());
    }

    public void setCategory(Category category) {
        this.setCategoryUUID(category.getUUID());
    }

    public int getCost() {
        return this.cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public CurrencyUnit getCurrencyUnit() {
        return this.currencyUnit;
    }

    public void setCurrencyUnit(CurrencyUnit currencyUnit) {
        this.currencyUnit = currencyUnit;
    }

    public int getWeight() {
        return this.weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public List<String> getAdditionalTags() {
        return additionalTags;
    }

    public void addAdditionalTag(String tag) {
        this.getAdditionalTags().add(tag);
    }

    public void removeAdditionalTag(String tag) {
        this.getAdditionalTags().remove(tag);
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isAttunement() {
        return this.attunement;
    }

    public void setAttunement(boolean attunement) {
        this.attunement = attunement;
    }

    public String getAttunementRequirements() {
        return this.attunementRequirements;
    }

    public void setAttunementRequirements(String attunementRequirements) {
        this.attunementRequirements = attunementRequirements;
    }

    public Document getMetadata() {
        return this.metadata;
    }
}
