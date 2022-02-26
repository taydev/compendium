package dev.compendium.core.spell;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.compendium.bot.CompendiumBot;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.CharacterClass;
import dev.compendium.core.character.component.Subclass;
import dev.compendium.core.util.*;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

import dev.compendium.core.util.entry.*;
import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

// TODO: literally everything
public class Spell {

    private UUID uuid;
    private String name;
    private String source;
    private int sourcePage;
    private boolean SRD;
    private int level;
    private String school;
    private int time;
    private TimeUnit timeUnit;
    private String rangeType;
    private int distance;
    private RangeUnit distanceType;
    private boolean verbal;
    private boolean somatic;
    private String material;
    private boolean ritual;
    private boolean timed;
    private int duration;
    private TimeUnit durationUnit;
    private boolean concentration;
    private List<Entry> entries;
    private List<Entry> entriesHigherLevel;
    private String scalingDamageType;
    private Map<Integer, String> scalingDamageDice;
    private List<String> tags;
    private List<ArchetypeEntry> classes;
    private List<SubArchetypeEntry> subclasses;
    private List<VariantArchetypeEntry> variants;
    private List<BaseArchetypeEntry> races;
    private List<ArchetypeEntry> backgrounds;
    private List<ArchetypeEntry> eldritchInvocations;

    //region 5e.tools format
    // THIS IS JUST HERE FOR THE SAKE OF AUTO-IMPORTING
    public Spell(JsonObject obj) {
        this.uuid = ElementRegistry.getInstance().createSpellUUID();
        this.name = obj.get("name").getAsString();
        this.source = obj.get("source").getAsString();
        this.sourcePage = obj.get("page").getAsInt();
        this.SRD = obj.get("srd").getAsBoolean();
        this.level = obj.get("level").getAsInt();
        this.school = obj.get("school").getAsString();
        JsonObject timeObject = obj.get("time").getAsJsonArray().get(0).getAsJsonObject();
        this.timeUnit = TimeUnit.getTime(timeObject.get("unit").getAsString());
        this.time = timeObject.get("number").getAsInt();
        JsonObject rangeObject = obj.get("range").getAsJsonObject();
        this.rangeType = rangeObject.get("type").getAsString();
        JsonObject distanceObject = rangeObject.get("distance").getAsJsonObject();
        this.distanceType = RangeUnit.getRange(distanceObject.get("type").getAsString());
        if (this.distanceType == RangeUnit.TOUCH) {
            this.distance = -1;
        } else {
            this.distance = distanceObject.get("amount").getAsInt();
        }
        JsonObject componentsObject = obj.get("components").getAsJsonObject();
        this.verbal = componentsObject.has("v");
        this.somatic = componentsObject.has("s");
        if (componentsObject.has("m")) {
            this.material = componentsObject.get("m").getAsString();
        } else {
            this.material = "";
        }
        if (obj.has("meta")) {
            JsonObject metaObject = obj.get("meta").getAsJsonObject();
            this.ritual = metaObject.has("ritual");
        } else {
            this.ritual = false;
        }
        JsonObject durationObject = obj.get("duration").getAsJsonArray().get(0).getAsJsonObject();
        this.timed = durationObject.get("type").getAsString().equalsIgnoreCase("timed");
        if (this.timed) {
            JsonObject innerDurationObject = durationObject.get("duration").getAsJsonObject();
            this.durationUnit = TimeUnit.getTime(innerDurationObject.get("type").getAsString());
            this.duration = innerDurationObject.get("amount").getAsInt();
            this.concentration = durationObject.has("concentration");
        } else {
            this.durationUnit = TimeUnit.INSTANTANEOUS;
            this.duration = -1;
            this.concentration = false;
        }
        JsonArray entries = obj.get("entries").getAsJsonArray();
        this.entries = ParseUtils.parseEntries(entries);
        if (obj.has("entriesHigherLevel")) {
            JsonArray entriesHigherLevel = obj.get("entriesHigherLevel").getAsJsonArray();
            this.entriesHigherLevel = ParseUtils.parseEntries(entriesHigherLevel);
        } else {
            this.entriesHigherLevel = new ArrayList<>();
        }
        if (obj.has("scalingLevelDice")) {
            JsonObject scalingLevelDice = obj.get("scalingLevelDice").getAsJsonObject();
            this.scalingDamageType = scalingLevelDice.get("label").getAsString();
            JsonObject scalingDamage = scalingLevelDice.get("scaling").getAsJsonObject();
            this.scalingDamageDice = new HashMap<>();
            for (Map.Entry<String, JsonElement> scalingDamageEntry : scalingDamage.entrySet()) {
                scalingDamageDice.put(
                        Integer.parseInt(scalingDamageEntry.getKey()),
                        scalingDamageEntry.getValue().getAsString()
                );
            }
        }
        this.tags = new ArrayList<>();
        if (obj.has("damageInflict")) {
            JsonArray damageInflictArray = obj.get("damageInflict").getAsJsonArray();
            for (JsonElement damageInflictElement : damageInflictArray) {
                this.tags.add(damageInflictElement.getAsString());
            }
        }
        if (obj.has("savingThrow")) {
            JsonArray savingThrowArray = obj.get("savingThrow").getAsJsonArray();
            for (JsonElement savingThrowElement : savingThrowArray) {
                this.tags.add(savingThrowElement.getAsString());
            }
        }
        if (obj.has("conditionInflict")) {
            JsonArray conditionInflictArray = obj.get("conditionInflict").getAsJsonArray();
            for (JsonElement conditionInflictElement : conditionInflictArray) {
                this.tags.add(conditionInflictElement.getAsString());
            }
        }
        if (obj.has("abilityCheck")) {
            JsonArray abilityCheckArray = obj.get("abilityCheck").getAsJsonArray();
            for (JsonElement abilityCheckElement : abilityCheckArray) {
                this.tags.add(abilityCheckElement.getAsString());
            }
        }
        if (obj.has("spellAttack")) {
            JsonArray spellAttackArray = obj.get("spellAttack").getAsJsonArray();
            for (JsonElement spellAttackElement : spellAttackArray) {
                this.tags.add(spellAttackElement.getAsString());
            }
        }
        if (obj.has("miscTags")) {
            JsonArray miscTagsArray = obj.get("miscTags").getAsJsonArray();
            for (JsonElement miscTagsElement : miscTagsArray) {
                this.tags.add(miscTagsElement.getAsString());
            }
        }
        if (obj.has("areaTags")) {
            JsonArray areaTagsArray = obj.get("areaTags").getAsJsonArray();
            for (JsonElement areaTagsElement : areaTagsArray) {
                this.tags.add(areaTagsElement.getAsString());
            }
        }
        JsonObject classesObj = obj.get("classes").getAsJsonObject();
        if (classesObj.has("fromClassList")) {
            this.classes = new ArrayList<>();
            JsonArray fromClassListArray = classesObj.get("fromClassList").getAsJsonArray();
            for (JsonElement classElement : fromClassListArray) {
                JsonObject classListEntryObj = classElement.getAsJsonObject();
                ArchetypeEntry archetypeEntry = new ArchetypeEntry(
                        classListEntryObj.get("name").getAsString(),
                        classListEntryObj.get("source").getAsString()
                );
                this.classes.add(archetypeEntry);
            }
        }
        if (classesObj.has("fromSubclass")) {
            this.subclasses = new ArrayList<>();
            JsonArray fromSubclassArray = classesObj.get("fromSubclass").getAsJsonArray();
            for (JsonElement subclassElement : fromSubclassArray) {
                JsonObject fromSubclassEntryObj = subclassElement.getAsJsonObject();
                JsonObject innerClassObj = fromSubclassEntryObj.get("class").getAsJsonObject();
                JsonObject innerSubclassObj = fromSubclassEntryObj.get("subclass").getAsJsonObject();
                SubArchetypeEntry subArchetypeEntry = new SubArchetypeEntry(
                        innerClassObj.get("name").getAsString(),
                        innerClassObj.get("source").getAsString(),
                        innerSubclassObj.get("name").getAsString(),
                        innerSubclassObj.get("source").getAsString()
                );
                if (innerSubclassObj.has("subSubclass")) {
                    subArchetypeEntry.setSubSubArchetypeName(innerSubclassObj.get("subSubclass").getAsString());
                }
                this.subclasses.add(subArchetypeEntry);
            }
        }
        if (classesObj.has("fromClassListVariant")) {
            this.variants = new ArrayList<>();
            JsonArray fromClassListVariantArray = classesObj.get("fromClassListVariant").getAsJsonArray();
            for (JsonElement classListVariantElement : fromClassListVariantArray) {
                JsonObject classListVariantObj = classListVariantElement.getAsJsonObject();
                VariantArchetypeEntry variantArchetypeEntry = new VariantArchetypeEntry(
                        classListVariantObj.get("name").getAsString(),
                        classListVariantObj.get("source").getAsString(),
                        classListVariantObj.get("definedInSource").getAsString()
                );
                this.variants.add(variantArchetypeEntry);
            }
        }
        if (obj.has("races")) {
            this.races = new ArrayList<>();
            JsonArray racesArray = obj.get("races").getAsJsonArray();
            for (JsonElement raceElement : racesArray) {
                JsonObject raceObj = raceElement.getAsJsonObject();
                BaseArchetypeEntry baseArchetypeEntry = new BaseArchetypeEntry(
                        raceObj.get("name").getAsString(),
                        raceObj.get("source").getAsString(),
                        raceObj.get("baseName").getAsString(),
                        raceObj.get("baseSource").getAsString()
                );
                this.races.add(baseArchetypeEntry);
            }
        }
        if (obj.has("backgrounds")) {
            this.backgrounds = new ArrayList<>();
            JsonArray backgroundArray = obj.get("backgrounds").getAsJsonArray();
            for (JsonElement backgroundElement : backgroundArray) {
                JsonObject backgroundObj = backgroundElement.getAsJsonObject();
                ArchetypeEntry archetypeEntry = new ArchetypeEntry(
                        backgroundObj.get("name").getAsString(),
                        backgroundObj.get("source").getAsString()
                );
                this.backgrounds.add(archetypeEntry);
            }
        }
        if (obj.has("eldritchInvocations")) {
            this.eldritchInvocations = new ArrayList<>();
            JsonArray eldritchInvocationArray = obj.get("eldritchInvocations").getAsJsonArray();
            for (JsonElement eldritchInvocationElement : eldritchInvocationArray) {
                JsonObject eldritchInvocationObj = eldritchInvocationElement.getAsJsonObject();
                ArchetypeEntry archetypeEntry = new ArchetypeEntry(
                        eldritchInvocationObj.get("name").getAsString(),
                        eldritchInvocationObj.get("source").getAsString()
                );
                this.eldritchInvocations.add(archetypeEntry);
            }
        }
    }
    // fucking hell this method hurts my soul ,w,

    public UUID getUUID() {
        return this.uuid;
    }

    public String getName() {
        return this.name;
    }

    public String getSource() {
        return this.source;
    }

    public int getSourcePage() {
        return this.sourcePage;
    }

    public boolean isInSRD() {
        return this.SRD;
    }

    public int getLevel() {
        return this.level;
    }

    public String getSchool() {
        return this.school;
    }

    public int getCastingTime() {
        return this.time;
    }

    public TimeUnit getCastingTimeUnit() {
        return this.timeUnit;
    }

    public String getRangeType() {
        return this.rangeType;
    }

    public RangeUnit getDistanceType() {
        return this.distanceType;
    }

    public int getDistance() {
        return this.distance;
    }

    public boolean hasVerbal() {
        return this.verbal;
    }

    public boolean hasSomatic() {
        return this.somatic;
    }

    public boolean hasMaterial() {
        return !this.material.equals("");
    }

    public String getMaterial() {
        return this.material;
    }

    public boolean isRitual() {
        return this.ritual;
    }

    public boolean isTimed() {
        return this.timed;
    }

    public TimeUnit getDurationUnit() {
        return this.durationUnit;
    }

    public int getDuration() {
        return this.duration;
    }

    public boolean isConcentration() {
        return this.concentration;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public List<Entry> getEntriesHigherLevel() {
        return this.entriesHigherLevel;
    }

    public String getScalingDamageType() {
        return this.scalingDamageType;
    }

    public Map<Integer, String> getScalingDamageDice() {
        return this.scalingDamageDice;
    }

    public List<String> getTags() {
        return this.tags;
    }

    public List<ArchetypeEntry> getClasses() {
        return this.classes;
    }

    public List<SubArchetypeEntry> getSubclasses() {
        return this.subclasses;
    }

    public List<VariantArchetypeEntry> getVariants() {
        return this.variants;
    }

    public List<ArchetypeEntry> getBackgrounds() {
        return this.backgrounds;
    }

    public List<BaseArchetypeEntry> getRaces() {
        return this.races;
    }

    public List<ArchetypeEntry> getEldritchInvocations() {
        return this.eldritchInvocations;
    }
    //endregion
}
