package dev.compendium.core.item;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: finish
public class Item {

    @BsonId
    private final UUID uuid;
    @BsonProperty("source_uuid")
    private final UUID sourceUUID;
    @BsonProperty("additional_tags")
    private final List<String> additionalTags;
    @BsonProperty("alternative_names")
    private final List<String> alternativeNames;
    private final Metadata metadata;
    private String name;
    private int quantity;
    @BsonProperty("category_uuid")
    private UUID categoryUUID;
    private int cost;
    @BsonProperty("currency_uuid")
    private UUID currencyUUID;
    private double weight;
    private String description;
    private boolean attunement;
    @BsonProperty("attunement_requirements")
    private String attunementRequirements;

    public Item(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Item(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createItemUUID(), sourceUUID, name, 1, (null), 0, (null), 0,
            new ArrayList<>(),
            new ArrayList<>(), "", false, "", new Metadata());
    }

    @BsonCreator
    public Item(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("quantity") int quantity, @BsonProperty("category_uuid") UUID categoryUUID,
        @BsonProperty("cost") int cost, @BsonProperty("currency_uuid") UUID currencyUUID,
        @BsonProperty("weight") double weight, @BsonProperty("additional_tags") List<String> additionalTags,
        @BsonProperty("alternative_names") List<String> alternativeNames,
        @BsonProperty("description") String description, @BsonProperty("attunement") boolean attunement,
        @BsonProperty("attunement_requirements") String attunementRequirements,
        @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.name = name;
        this.quantity = quantity;
        this.sourceUUID = sourceUUID;
        this.categoryUUID = categoryUUID;
        this.cost = cost;
        this.currencyUUID = currencyUUID;
        this.weight = weight;
        this.additionalTags = additionalTags;
        this.alternativeNames = alternativeNames;
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

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @BsonProperty("category_uuid")
    public UUID getCategoryUUID() {
        return this.categoryUUID;
    }

    public void setCategoryUUID(UUID categoryUUID) {
        this.categoryUUID = categoryUUID;
    }

    @BsonIgnore
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

    @BsonProperty("currency_uuid")
    public UUID getCurrencyUUID() {
        return this.currencyUUID;
    }

    public void setCurrencyUUID(UUID currencyUUID) {
        this.currencyUUID = currencyUUID;
    }

    @BsonIgnore
    public Currency getCurrency() {
        return ElementRegistry.getInstance().getCurrencyByUUID(this.getCurrencyUUID());
    }

    public void setCurrency(Currency currency) {
        this.setCurrencyUUID(currency.getUUID());
    }

    public double getWeight() {
        return this.weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    @BsonProperty("additional_tags")
    public List<String> getAdditionalTags() {
        return this.additionalTags;
    }

    public void addAdditionalTag(String tag) {
        this.getAdditionalTags().add(tag);
    }

    public void removeAdditionalTag(String tag) {
        this.getAdditionalTags().remove(tag);
    }

    @BsonProperty("alternative_names")
    public List<String> getAlternativeNames() {
        return this.alternativeNames;
    }

    public void addAlternativeName(String name) {
        this.alternativeNames.add(name);
    }

    public void removeAlternativeName(String name) {
        this.alternativeNames.remove(name);
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

    @BsonProperty("attunement_requirements")
    public String getAttunementRequirements() {
        return this.attunementRequirements;
    }

    public void setAttunementRequirements(String attunementRequirements) {
        this.attunementRequirements = attunementRequirements;
    }

    public Metadata getMetadata() {
        return this.metadata;
    }
}
