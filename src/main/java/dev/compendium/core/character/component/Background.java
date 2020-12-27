package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.item.Item;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Background {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<UUID> proficiencyUUIDs;
    private final List<UUID> languageUUIDs;
    private final Map<UUID, Integer> equipmentUUIDs;
    private final Map<String, String> features;
    private String name;
    private String description;

    public Background(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createBackgroundUUID(), sourceUUID, name, "", new ArrayList<>(),
            new ArrayList<>(), new HashMap<>(), new HashMap<>());
    }

    @BsonCreator
    public Background(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("description") String description, @BsonProperty("proficiencies") List<UUID> proficiencyUUIDs,
        @BsonProperty("languages") List<UUID> languageUUIDs,
        @BsonProperty("equipment") Map<UUID, Integer> equipmentUUIDs,
        @BsonProperty("features") Map<String, String> features) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.proficiencyUUIDs = proficiencyUUIDs;
        this.languageUUIDs = languageUUIDs;
        this.equipmentUUIDs = equipmentUUIDs;
        this.features = features;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<UUID> getProficiencyUUIDs() {
        return this.proficiencyUUIDs;
    }

    public List<Proficiency> getProficiencies() {
        List<Proficiency> proficiencies = new ArrayList<>();
        for (UUID uuid : this.getProficiencyUUIDs()) {
            proficiencies.add(ElementRegistry.getInstance().getProficiencyByUUID(uuid));
        }
        return proficiencies;
    }

    public void addProficiency(Proficiency proficiency) {
        this.getProficiencyUUIDs().add(proficiency.getUUID());
    }

    public void removeProficiency(Proficiency proficiency) {
        this.getProficiencyUUIDs().remove(proficiency.getUUID());
    }

    public boolean hasProficiency(Proficiency proficiency) {
        return this.hasProficiency(proficiency.getUUID());
    }

    public boolean hasProficiency(UUID uuid) {
        return this.getProficiencyUUIDs().contains(uuid);
    }

    public List<UUID> getLanguageUUIDs() {
        return this.languageUUIDs;
    }

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

    public boolean hasLanguage(Language language) {
        return this.hasLanguage(language.getUUID());
    }

    public boolean hasLanguage(UUID uuid) {
        return this.getLanguageUUIDs().contains(uuid);
    }

    public Map<UUID, Integer> getEquipmentUUIDs() {
        return this.equipmentUUIDs;
    }

    public Map<Item, Integer> getEquipment() {
        Map<Item, Integer> equipment = new HashMap<>();
        for (Map.Entry<UUID, Integer> entry : this.equipmentUUIDs.entrySet()) {
            equipment.put(ElementRegistry.getInstance().getItemByUUID(entry.getKey()), entry.getValue());
        }
        return equipment;
    }

    public void addEquipment(Item item, int amount) {
        this.addEquipment(item.getUUID(), amount);
    }

    public void addEquipment(UUID uuid, int amount) {
        this.getEquipmentUUIDs().put(uuid, amount);
    }

    public void removeEquipment(Item item) {
        this.removeEquipment(item.getUUID());
    }

    public void removeEquipment(UUID uuid) {
        this.getEquipmentUUIDs().remove(uuid);
    }

    public Map<String, String> getFeatures() {
        return this.features;
    }

    public void addFeature(String featureName, String featureDescription) {
        this.getFeatures().put(featureName, featureDescription);
    }

    public void removeFeature(String featureName) {
        this.getFeatures().remove(featureName);
    }
}
