package dev.compendium.core.spell;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.CharacterClass;
import dev.compendium.core.util.RangeUnit;
import dev.compendium.core.util.Source;
import dev.compendium.core.util.TimeUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: write documentation
public class Spell {

    @BsonId
    private final UUID uuid;
    private final UUID sourceUUID;
    private final List<String> additionalTags;
    private final List<UUID> classUUIDs;
    private final Document metadata;
    private String name;
    private int level;
    private UUID schoolUUID;
    private boolean ritual;
    private int castingDuration;
    private TimeUnit timeUnit;
    private int castingRange;
    private RangeUnit rangeUnit;
    private int duration;
    private TimeUnit durationUnit;
    private boolean concentration;
    private boolean verbalComponent;
    private boolean somaticComponent;
    private boolean materialComponent;
    private String materials;
    private String description;

    public Spell(UUID sourceUUID, String name) {
        this(ElementRegistry.getInstance().createSpellUUID(), sourceUUID, name, 0, UUID.randomUUID(), false,
            new ArrayList<>(), 1, TimeUnit.ACTION, 30, RangeUnit.FOOT, 0, TimeUnit.ACTION, false, false, false, false,
            "", new ArrayList<>(), "", new Document());
    }

    @BsonCreator
    public Spell(@BsonId UUID uuid, @BsonProperty("sourceUuid") UUID sourceUUID, @BsonProperty("name") String name,
        @BsonProperty("level") int level, @BsonProperty("schoolUuid") UUID schoolUUID,
        @BsonProperty("ritual") boolean ritual, @BsonProperty("additionalTags") List<String> additionalTags,
        @BsonProperty("castingDuration") int castingDuration, @BsonProperty("castingTimeUnit") TimeUnit timeUnit,
        @BsonProperty("castingRange") int castingRange, @BsonProperty("castingRangeUnit") RangeUnit rangeUnit,
        @BsonProperty("duration") int duration, @BsonProperty("durationUnit") TimeUnit durationUnit,
        @BsonProperty("concentration") boolean concentration, @BsonProperty("verbalComponent") boolean verbalComponent,
        @BsonProperty("somaticComponent") boolean somaticComponent,
        @BsonProperty("materialComponent") boolean materialComponent, @BsonProperty("materials") String materials,
        @BsonProperty("classes") List<UUID> classUUIDs, @BsonProperty("description") String description,
        @BsonProperty("metadata") Document metadata) {
        this.uuid = uuid;
        this.name = name;
        this.sourceUUID = sourceUUID;
        this.level = level;
        this.schoolUUID = schoolUUID;
        this.ritual = ritual;
        this.additionalTags = additionalTags;
        this.castingDuration = castingDuration;
        this.timeUnit = timeUnit;
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
        this.description = description;
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

    public int getLevel() {
        return this.level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public UUID getSchoolUUID() {
        return this.schoolUUID;
    }

    public void setSchoolUUID(UUID schoolUUID) {
        this.schoolUUID = schoolUUID;
    }

    public MagicSchool getSchool() {
        return ElementRegistry.getInstance().getMagicSchoolByUUID(this.getSchoolUUID());
    }

    public void setSchool(MagicSchool school) {
        this.setSchoolUUID(school.getUUID());
    }

    public boolean isRitual() {
        return this.ritual;
    }

    public void setRitual(boolean ritual) {
        this.ritual = ritual;
    }

    public List<String> getAdditionalTags() {
        return this.additionalTags;
    }

    public void addTag(String tag) {
        this.getAdditionalTags().add(tag);
    }

    public void removeTag(String tag) {
        this.getAdditionalTags().remove(tag);
    }

    public int getCastingDuration() {
        return this.castingDuration;
    }

    public void setCastingDuration(int duration) {
        this.castingDuration = duration;
    }

    public TimeUnit getCastingTimeUnit() {
        return this.timeUnit;
    }

    public void setCastingTimeUnit(TimeUnit timeUnit) {
        this.timeUnit = timeUnit;
    }

    public int getCastingRange() {
        return this.castingRange;
    }

    public void setCastingRange(int range) {
        this.castingRange = range;
    }

    public RangeUnit getCastingRangeUnit() {
        return this.rangeUnit;
    }

    public void setCastingRangeUnit(RangeUnit rangeUnit) {
        this.rangeUnit = rangeUnit;
    }

    public int getDuration() {
        return this.duration;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public TimeUnit getDurationUnit() {
        return this.durationUnit;
    }

    public void setDurationUnit(TimeUnit durationUnit) {
        this.durationUnit = durationUnit;
    }

    public boolean isConcentration() {
        return this.concentration;
    }

    public void setConcentration(boolean concentration) {
        this.concentration = concentration;
    }

    public boolean isVerbalComponent() {
        return this.verbalComponent;
    }

    public void setVerbalComponent(boolean verbalComponent) {
        this.verbalComponent = verbalComponent;
    }

    public boolean isSomaticComponent() {
        return this.somaticComponent;
    }

    public void setSomaticComponent(boolean somaticComponent) {
        this.somaticComponent = somaticComponent;
    }

    public boolean isMaterialComponent() {
        return this.materialComponent;
    }

    public void setMaterialComponent(boolean materialComponent) {
        this.materialComponent = materialComponent;
    }

    public String getMaterials() {
        return this.materials;
    }

    public void setMaterials(String materials) {
        this.materials = materials;
    }

    public List<UUID> getClassUUIDs() {
        return this.classUUIDs;
    }

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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Document getMetadata() {
        return this.metadata;
    }
}
