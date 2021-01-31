package dev.compendium.core.spell;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.CharacterClass;
import dev.compendium.core.character.component.Subclass;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.RangeUnit;
import dev.compendium.core.util.Source;
import dev.compendium.core.util.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: write documentation
public class Spell {

    @BsonId
    private final UUID uuid;
    @BsonProperty("source_uuid")
    private final UUID sourceUUID;
    @BsonProperty("additional_tags")
    private final List<String> additionalTags;
    @BsonProperty("class_uuids")
    private final List<UUID> classUUIDs;
    @BsonProperty("subclass_uuids")
    private final List<UUID> subclassUUIDs;
    @BsonProperty("metadata")
    private final Metadata metadata;
    @BsonProperty("name")
    private String name;
    @BsonProperty("level")
    private int level;
    @BsonProperty("school_uuid")
    private UUID schoolUUID;
    @BsonProperty("ritual")
    private boolean ritual;
    @BsonProperty("casting_duration")
    private int castingDuration;
    @BsonProperty("casting_time_unit")
    private TimeUnit timeUnit;
    @BsonProperty("reaction_condition")
    private String reactionCondition;
    @BsonProperty("casting_range")
    private int castingRange;
    @BsonProperty("casting_range_unit")
    private RangeUnit rangeUnit;
    @BsonProperty("duration")
    private int duration;
    @BsonProperty("duration_time_unit")
    private TimeUnit durationUnit;
    @BsonProperty("concentration")
    private boolean concentration;
    @BsonProperty("verbal_component")
    private boolean verbalComponent;
    @BsonProperty("somatic_component")
    private boolean somaticComponent;
    @BsonProperty("material_component")
    private boolean materialComponent;
    @BsonProperty("materials")
    private String materials;
    @BsonProperty("description")
    private String description;
    @BsonProperty("upcast_description")
    private String upcastDescription;

    public Spell(Source source, String name) {
        this(source.getUUID(), name);
    }

    public Spell(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createSpellUUID(), sourceUUID, name, 0, UUID.randomUUID(), false,
            new ArrayList<>(), 1, TimeUnit.ACTION, "", 30, RangeUnit.FOOT, 0, TimeUnit.ACTION, false, false, false, false,
            "", new ArrayList<>(), new ArrayList<>(), "", "", new Metadata());
    }

    @BsonCreator
    public Spell(@BsonId UUID uuid, @BsonProperty("source_uuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("level") int level, @BsonProperty("school_uuid") UUID schoolUUID,
        @BsonProperty("ritual") boolean ritual, @BsonProperty("additional_tags") List<String> additionalTags,
        @BsonProperty("casting_duration") int castingDuration, @BsonProperty("casting_time_unit") TimeUnit timeUnit,
        @BsonProperty("reaction_condition") String reactionCondition, @BsonProperty("casting_range") int castingRange,
        @BsonProperty("casting_range_unit") RangeUnit rangeUnit, @BsonProperty("duration") int duration,
        @BsonProperty("duration_time_unit") TimeUnit durationUnit, @BsonProperty("concentration") boolean concentration,
        @BsonProperty("verbal_component") boolean verbalComponent, @BsonProperty("somatic_component") boolean somaticComponent,
        @BsonProperty("material_component") boolean materialComponent, @BsonProperty("materials") String materials,
        @BsonProperty("class_uuids") List<UUID> classUUIDs, @BsonProperty("subclass_uuids") List<UUID> subclassUUIDs,
        @BsonProperty("description") String description, @BsonProperty("upcast_description") String upcastDescription,
        @BsonProperty("metadata") Metadata metadata) {
        this.uuid = uuid;
        this.name = name;
        this.sourceUUID = sourceUUID;
        this.level = level;
        this.schoolUUID = schoolUUID;
        this.ritual = ritual;
        this.additionalTags = additionalTags;
        this.castingDuration = castingDuration;
        this.timeUnit = timeUnit;
        this.reactionCondition = reactionCondition;
        this.castingRange = castingRange;
        this.rangeUnit = rangeUnit;
        this.duration = duration;
        this.durationUnit = durationUnit;
        this.concentration = concentration;
        this.verbalComponent = verbalComponent;
        this.somaticComponent = somaticComponent;
        this.materialComponent = materialComponent;
        this.materials = materials;
        this.classUUIDs = classUUIDs;
        this.subclassUUIDs = subclassUUIDs;
        this.description = description;
        this.upcastDescription = upcastDescription;
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

    @BsonProperty("level")
    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    @BsonProperty("school_uuid")
    public UUID getSchoolUUID() {
        return this.schoolUUID;
    }

    public void setSchoolUUID(UUID schoolUUID) {
        this.schoolUUID = schoolUUID;
    }

    @BsonIgnore
    public MagicSchool getSchool() {
        return ElementRegistry.getInstance().getMagicSchoolByUUID(this.getSchoolUUID());
    }

    public void setSchool(MagicSchool school) {
        this.setSchoolUUID(school.getUUID());
    }

    @BsonProperty("ritual")
    public boolean isRitual() {
        return this.ritual;
    }

    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }

    @BsonProperty("additional_tags")
    public List<String> getAdditionalTags() {
        return this.additionalTags;
    }

    public void addTag(String tag) {
        this.getAdditionalTags().add(tag);
    }

    public void removeTag(String tag) {
        this.getAdditionalTags().remove(tag);
    }

    @BsonProperty("casting_duration")
    public int getCastingDuration() {
        return this.castingDuration;
    }

    public void setCastingDuration(int duration) {
        this.castingDuration = duration;
    }

    @BsonProperty("casting_time_unit")
    public TimeUnit getCastingTimeUnit() {
        return this.timeUnit;
    }

    public void setCastingTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    @BsonProperty("reaction_condition")
    public String getReactionCondition() {
        return this.reactionCondition;
    }

    public void setReactionCondition(String reactionCondition) {
        this.reactionCondition = reactionCondition;
    }

    @BsonProperty("casting_range")
    public int getCastingRange() {
        return this.castingRange;
    }

    public void setCastingRange(int range) {
        this.castingRange = range;
    }

    @BsonProperty("casting_range_unit")
    public RangeUnit getCastingRangeUnit() {
        return this.rangeUnit;
    }

    public void setCastingRangeUnit(RangeUnit rangeUnit) {
        this.rangeUnit = rangeUnit;
    }

    @BsonProperty("duration")
    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    @BsonProperty("duration_time_unit")
    public TimeUnit getDurationUnit() {
        return this.durationUnit;
    }

    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    @BsonProperty("concentration")
    public boolean isConcentration() {
        return this.concentration;
    }

    public void setConcentration(boolean concentration) {
        this.concentration = concentration;
    }

    @BsonProperty("verbal_component")
    public boolean isVerbalComponent() {
        return this.verbalComponent;
    }

    public void setVerbalComponent(boolean verbalComponent) {
        this.verbalComponent = verbalComponent;
    }

    @BsonProperty("somatic_component")
    public boolean isSomaticComponent() {
        return this.somaticComponent;
    }

    public void setSomaticComponent(boolean somaticComponent) {
        this.somaticComponent = somaticComponent;
    }

    @BsonProperty("material_component")
    public boolean isMaterialComponent() {
        return this.materialComponent;
    }

    public void setMaterialComponent(boolean materialComponent) {
        this.materialComponent = materialComponent;
    }

    @BsonProperty("materials")
    public String getMaterials() {
        return this.materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    @BsonProperty("class_uuids")
    public List<UUID> getClassUUIDs() {
        return this.classUUIDs;
    }

    @BsonIgnore
    public List<CharacterClass> getClasses() {
        List<CharacterClass> classes = new ArrayList<>();
        for (UUID uuid : this.getClassUUIDs()) {
            classes.add(ElementRegistry.getInstance().getClassByUUID(uuid));
        }
        return classes;
    }

    public void addClass(CharacterClass characterClass) {
        this.addClass(characterClass.getUUID());
    }

    public void addClass(UUID uuid) {
        this.getClassUUIDs().add(uuid);
    }

    public void removeClass(CharacterClass characterClass) {
        this.removeClass(characterClass.getUUID());
    }

    public void removeClass(UUID uuid) {
        this.getClassUUIDs().remove(uuid);
    }

    @BsonProperty("subclass_uuids")
    public List<UUID> getSubclassUUIDs() {
        return this.subclassUUIDs;
    }

    @BsonIgnore
    public List<Subclass> getSubclasses() {
        List<Subclass> subclasses = new ArrayList<>();
        for (UUID uuid : this.getSubclassUUIDs()) {
            subclasses.add(ElementRegistry.getInstance().getSubclassByUUID(uuid));
        }
        return subclasses;
    }

    public void addSubclass(Subclass subclass) {
        this.addSubclass(subclass.getUUID());
    }

    public void addSubclass(UUID uuid) {
        this.getSubclassUUIDs().add(uuid);
    }

    public void removeSubclass(Subclass subclass) {
        this.removeSubclass(subclass.getUUID());
    }

    public void removeSubclass(UUID uuid) {
        this.getSubclassUUIDs().remove(uuid);
    }

    @BsonProperty("description")
    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @BsonProperty("upcast_description")
    public String getUpcastDescription() {
        return this.upcastDescription;
    }

    public void setUpcastDescription(String upcastDescription) {
        this.upcastDescription = upcastDescription;
    }

    @BsonProperty("metadata")
    public Metadata getMetadata() {
        return this.metadata;
    }
}
