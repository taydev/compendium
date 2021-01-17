package dev.compendium.core.character.component;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.item.Item;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class CharacterClass {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<UUID> proficiencyUUIDs;
    private final List<UUID> equipmentUUIDs;
    private final List<UUID> featureUUIDs;
    private final Metadata metadata;
    private String name;
    private String description;
    private String hitDice;
    private UUID subclassUUID;

    public CharacterClass(Source source, String name) {
        this(source.getUUID(), name);
    }

    public CharacterClass(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createClassUUID(), sourceUUID, name, "", "", new ArrayList<>(),
            new ArrayList<>(), new ArrayList<>(), null, new Metadata());
    }

    @BsonCreator
    public CharacterClass(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID,
        @BsonProperty("name") String name, @BsonProperty("description") String description,
        @BsonProperty("hit_dice") String hitDice, @BsonProperty("proficiency_uuids") List<UUID> proficiencyUUIDs,
        @BsonProperty("equipment_uuids") List<UUID> equipmentUUIDs,
        @BsonProperty("feature_uuids") List<UUID> featureUUIDs,
        @BsonProperty("subclass_uuid") UUID subclassUUID, @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.sourceUUID = sourceUUID;
        this.name = name;
        this.description = description;
        this.hitDice = hitDice;
        this.proficiencyUUIDs = proficiencyUUIDs;
        this.equipmentUUIDs = equipmentUUIDs;
        this.featureUUIDs = featureUUIDs;
        this.subclassUUID = subclassUUID;
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getHitDice() {
        return this.hitDice;
    }

    public void setHitDice(String hitDice) {
        this.hitDice = hitDice;
    }

    public List<UUID> getProficiencyUUIDs() {
        return this.proficiencyUUIDs;
    }

    @BsonIgnore
    public List<Proficiency> getProficiencies() {
        List<Proficiency> result = new ArrayList<>();
        for (UUID uuid : this.getProficiencyUUIDs()) {
            result.add(ElementRegistry.getInstance().getProficiencyByUUID(uuid));
        }
        return result;
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

    public boolean hasProficiency(Proficiency proficiency) {
        return this.hasProficiency(proficiency.getUUID());
    }

    public boolean hasProficiency(UUID uuid) {
        return this.getProficiencyUUIDs().contains(uuid);
    }

    public List<UUID> getEquipmentUUIDs() {
        return this.equipmentUUIDs;
    }

    @BsonIgnore
    public List<Item> getEquipment() {
        List<Item> result = new ArrayList<>();
        for (UUID uuid : this.getEquipmentUUIDs()) {
            result.add(ElementRegistry.getInstance().getItemByUUID(uuid));
        }
        return result;
    }

    public void addEquipment(Item item) {
        this.addEquipment(item.getUUID());
    }

    public void addEquipment(UUID uuid) {
        this.getEquipmentUUIDs().add(uuid);
    }

    public void removeEquipment(Item item) {
        this.removeEquipment(item.getUUID());
    }

    public void removeEquipment(UUID uuid) {
        this.getEquipmentUUIDs().remove(uuid);
    }

    public List<UUID> getFeatureUUIDs() {
        return this.featureUUIDs;
    }

    @BsonIgnore
    public List<Feature> getClassFeatures() {
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

    public UUID getSubclassUUID() {
        return this.subclassUUID;
    }

    public void setSubclassUUID(UUID subclassUUID) {
        this.subclassUUID = subclassUUID;
    }

    @BsonIgnore
    public Subclass getSubclass() {
        return ElementRegistry.getInstance().getSubclassByUUID(this.getSubclassUUID());
    }

    public Metadata getMetadata() {
        return this.metadata;
    }
}


