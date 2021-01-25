package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.item.Item;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: write documentation
public class Background {

    @BsonId
    private final UUID uuid;
    @BsonProperty("source_uuid")
    private final UUID sourceUUID;
    @BsonProperty("proficiency_uuids")
    private final List<UUID> proficiencyUUIDs;
    @BsonProperty("language_uuids")
    private final List<UUID> languageUUIDs;
    @BsonProperty("equipment_uuids")
    private final Map<String, Integer> equipmentUUIDs;
    @BsonProperty("feature_uuids")
    private final List<UUID> featureUUIDs;
    @BsonProperty("suggested_traits")
    private final List<String> suggestedTraits;
    @BsonProperty("suggested_ideals")
    private final List<String> suggestedIdeals;
    @BsonProperty("suggested_bonds")
    private final List<String> suggestedBonds;
    @BsonProperty("suggested_flaws")
    private final List<String> suggestedFlaws;
    @BsonProperty("metadata")
    private final Metadata metadata;
    @BsonProperty("name")
    private String name;
    @BsonProperty("description")
    private String description;

    public Background(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Background(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createBackgroundUUID(), sourceUUID, name, "", new ArrayList<>(),
            new ArrayList<>(), new HashMap<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), new Metadata());
    }

    @BsonCreator
    public Background(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name,
        @BsonProperty("description") String description, @BsonProperty("proficiency_uuids") List<UUID> proficiencyUUIDs,
        @BsonProperty("language_uuids") List<UUID> languageUUIDs,
        @BsonProperty("equipment_uuids") Map<String, Integer> equipmentUUIDs,
        @BsonProperty("feature_uuids") List<UUID> featureUUIDs,
        @BsonProperty("suggested_traits") List<String> suggestedTraits,
        @BsonProperty("suggested_ideals") List<String> suggestedIdeals,
        @BsonProperty("suggested_bonds") List<String> suggestedBonds,
        @BsonProperty("suggested_flaws") List<String> suggestedFlaws,
        @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.proficiencyUUIDs = proficiencyUUIDs;
        this.languageUUIDs = languageUUIDs;
        this.equipmentUUIDs = equipmentUUIDs;
        this.featureUUIDs = featureUUIDs;
        this.suggestedTraits = suggestedTraits;
        this.suggestedIdeals = suggestedIdeals;
        this.suggestedBonds = suggestedBonds;
        this.suggestedFlaws = suggestedFlaws;
        this.metadata = metadata;
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

    @BsonProperty("description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @BsonProperty("proficiency_uuids")
    public List<UUID> getProficiencyUUIDs() {
        return this.proficiencyUUIDs;
    }

    @BsonIgnore
    public List<Proficiency> getProficiencies() {
        List<Proficiency> proficiencies = new ArrayList<>();
        for (UUID uuid : this.getProficiencyUUIDs()) {
            proficiencies.add(ElementRegistry.getInstance().getProficiencyByUUID(uuid));
        }
        return proficiencies;
    }

    public void addProficiency(Proficiency proficiency) {
        this.addProficiency(proficiency.getUUID());
    }

    public void addProficiency(UUID uuid) {
        this.getProficiencyUUIDs().add(uuid);
    }

    public void removeProficiency(Proficiency proficiency) {
        this.removeProficiency(proficiency.getUUID());
    }

    public void removeProficiency(UUID uuid) {
        this.getProficiencyUUIDs().remove(uuid);
    }

    @BsonProperty("language_uuids")
    public List<UUID> getLanguageUUIDs() {
        return this.languageUUIDs;
    }

    @BsonIgnore
    public List<Language> getLanguages() {
        List<Language> languages = new ArrayList<>();
        for (UUID uuid : this.getLanguageUUIDs()) {
            languages.add(ElementRegistry.getInstance().getLanguageByUUID(uuid));
        }
        return languages;
    }

    public void addLanguage(Language language) {
        this.addLanguage(language.getUUID());
    }

    public void addLanguage(UUID uuid) {
        this.getLanguageUUIDs().add(uuid);
    }

    public void removeLanguage(Language language) {
        this.removeLanguage(language.getUUID());
    }

    public void removeLanguage(UUID uuid) {
        this.getLanguageUUIDs().remove(uuid);
    }

    @BsonProperty("equipment_uuids")
    public Map<String, Integer> getEquipmentUUIDs() {
        return this.equipmentUUIDs;
    }

    @BsonIgnore
    public Map<Item, Integer> getEquipment() {
        Map<Item, Integer> equipment = new HashMap<>();
        for (Map.Entry<String, Integer> entry : this.equipmentUUIDs.entrySet()) {
            equipment.put(ElementRegistry.getInstance().getItemByUUID(UUID.fromString(entry.getKey())), entry.getValue());
        }
        return equipment;
    }

    public void addEquipment(Item item, int amount) {
        this.addEquipment(item.getUUID(), amount);
    }

    public void addEquipment(UUID uuid, int amount) {
        this.getEquipmentUUIDs().put(uuid.toString(), amount);
    }

    public void removeEquipment(Item item) {
        this.removeEquipment(item.getUUID());
    }

    public void removeEquipment(UUID uuid) {
        this.getEquipmentUUIDs().remove(uuid.toString());
    }

    @BsonProperty("feature_uuids")
    public List<UUID> getFeatureUUIDs() {
        return this.featureUUIDs;
    }

    @BsonIgnore
    public List<Feature> getFeatures() {
        List<Feature> result = new ArrayList<>();
        for (UUID uuid : this.getFeatureUUIDs()) {
            result.add(ElementRegistry.getInstance().getFeatureByUUID(uuid));
        }
        return result;
    }

    public void addFeature(Feature feature) {
        this.addFeature(feature.getUUID());
    }

    public void addFeature(UUID uuid) {
        this.getFeatureUUIDs().add(uuid);
    }

    public void removeFeature(Feature feature) {
        this.removeFeature(feature.getUUID());
    }

    public void removeFeature(UUID uuid) {
        this.getFeatureUUIDs().remove(uuid);
    }

    @BsonProperty("suggested_traits")
    public List<String> getSuggestedTraits() {
        return this.suggestedTraits;
    }

    public void addSuggestedTrait(String trait) {
        this.getSuggestedTraits().add(trait);
    }

    public void removeSuggestedTrait(String trait) {
        this.getSuggestedTraits().remove(trait);
    }

    @BsonProperty("suggested_ideals")
    public List<String> getSuggestedIdeals() {
        return this.suggestedIdeals;
    }

    public void addSuggestedIdeal(String ideal) {
        this.getSuggestedIdeals().add(ideal);
    }

    public void removeSuggestedIdeal(String ideal) {
        this.getSuggestedIdeals().remove(ideal);
    }

    @BsonProperty("suggested_bonds")
    public List<String> getSuggestedBonds() {
        return this.suggestedBonds;
    }

    public void addSuggestedBond(String bond) {
        this.getSuggestedBonds().add(bond);
    }

    public void removeSuggestedBond(String bond) {
        this.getSuggestedBonds().remove(bond);
    }

    @BsonProperty("suggested_flaws")
    public List<String> getSuggestedFlaws() {
        return this.suggestedFlaws;
    }

    public void addSuggestedFlaw(String flaw) {
        this.getSuggestedFlaws().add(flaw);
    }

    public void removeSuggestedFlaw(String flaw) {
        this.getSuggestedFlaws().remove(flaw);
    }

    @BsonProperty("metadata")
    public Metadata getMetadata() {
        return this.metadata;
    }
}
