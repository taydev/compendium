package dev.compendium.core;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.mongodb.client.model.Filters;
import com.mongodb.client.model.TextSearchOptions;
import dev.compendium.core.character.component.Alignment;
import dev.compendium.core.character.component.Background;
import dev.compendium.core.character.component.CharacterClass;
import dev.compendium.core.character.component.Feat;
import dev.compendium.core.character.component.Feature;
import dev.compendium.core.character.component.Language;
import dev.compendium.core.character.component.Proficiency;
import dev.compendium.core.character.component.Race;
import dev.compendium.core.character.component.Subclass;
import dev.compendium.core.item.Category;
import dev.compendium.core.item.Currency;
import dev.compendium.core.item.Item;
import dev.compendium.core.spell.MagicSchool;
import dev.compendium.core.spell.Spell;
import dev.compendium.core.util.Choice;
import dev.compendium.core.util.Creator;
import dev.compendium.core.util.ElementUtils;
import dev.compendium.core.util.Source;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ElementRegistry {

    private final static Logger LOGGER = LoggerFactory.getLogger(ElementRegistry.class);
    private static ElementRegistry instance;
    private final TextSearchOptions CASE_INSENSITIVE = new TextSearchOptions().caseSensitive(false)
        .diacriticSensitive(false);

    private final MongoClient client;
    private final CodecRegistry codecRegistry;

    //region Constructors and Instance Management Functions
    public ElementRegistry() {
        LOGGER.info("Initialising database connections...");
        this.client = new MongoClient();
        this.codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true)
                .register(ClassModel.builder(Source.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(MagicSchool.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Spell.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Category.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Item.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Currency.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Creator.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Background.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Choice.class).enableDiscriminator(true).build())
                .build()));
        LOGGER.info("Database connections initialised.");
    }

    public static ElementRegistry getInstance() {
        if (instance == null) {
            instance = new ElementRegistry();
        }
        return instance;
    }

    public void cleanup() {
        this.client.close();
        instance = null;
    }

    public static Logger getLogger() {
        return LOGGER;
    }
    //endregion

    //region Alignment Functions
    public Alignment getAlignmentByUUID(UUID uuid) {
        return this.getAlignments()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Alignment> findAlignmentsByKeyword(String keyword) {
        return StreamSupport.stream(this.getAlignments()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Background Functions
    public Background getBackgroundByUUID(UUID uuid) {
        return this.getBackgrounds()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Background> findBackgroundsByName(String name) {
        name = ElementUtils.capitalise(name);
        LOGGER.info("Finding background with name: {}", name);
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("name", name)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByKeyword(String keyword) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByProficiency(UUID proficiencyUUID) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("proficiency_uuids", proficiencyUUID)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByLanguage(UUID languageUUID) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("language_uuids", languageUUID)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByEquipment(UUID equipmentUUID) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("equipment_uuids", equipmentUUID)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Class Functions
    public CharacterClass getClassByUUID(UUID uuid) {
        return this.getClasses()
            .find(Filters.eq("_id", uuid))
            .first();
    }
    //endregion

    //region Feature Functions
    public Feature getFeatureByUUID(UUID uuid) {
        return this.getFeatures()
            .find(Filters.eq("_id", uuid))
            .first();
    }
    //endregion

    //region Subclass Functions
    public Subclass getSubclassByUUID(UUID uuid) {
        return this.getSubclasses()
            .find(Filters.eq("_id", uuid))
            .first();
    }
    //endregion

    //region Feat Functions
    public Feat getFeatByUUID(UUID uuid) {
        return this.getFeats()
            .find(Filters.eq("_id", uuid))
            .first();
    }
    //endregion

    //region Language Functions
    public Language getLanguageByUUID(UUID uuid) {
        return this.getLanguages()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Language> getAllLanguages() {
        return StreamSupport.stream(this.getLanguages().find().spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<UUID> getAllLanguageUUIDs() {
        return StreamSupport.stream(this.getLanguages().find().spliterator(), false)
            .map(Language::getUUID).collect(Collectors.toList());
    }

    public List<Language> findLanguagesByKeyword(String keyword) {
        return StreamSupport.stream(this.getLanguages()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Proficiency Functions
    public Proficiency getProficiencyByUUID(UUID uuid) {
        return this.getProficiencies()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Proficiency> findProficienciesByKeyword(String keyword) {
        return StreamSupport.stream(this.getProficiencies()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Race Functions
    public Race getRaceByUUID(UUID uuid) {
        return this.getRaces()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Race> findRacesByKeyword(String keyword) {
        return StreamSupport.stream(this.getRaces()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Currency Functions
    public Currency getCurrencyByUUID(UUID uuid) {
        return this.getCurrencies()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Currency> findCurrenciesByAbbreviation(String abbreviation) {
        return StreamSupport.stream(this.getCurrencies()
            .find(Filters.eq("abbreviation", abbreviation)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Currency> findCurrenciesByKeyword(String keyword) {
        return StreamSupport.stream(this.getCurrencies()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Category Functions
    public Category getCategoryByUUID(UUID uuid) {
        return this.getCategories()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Category> findCategoriesByName(String name) {
        return StreamSupport.stream(this.getCategories()
            .find(Filters.text(name, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Item Functions
    public Item getItemByUUID(UUID uuid) {
        return this.getItems()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Item> findItemsByName(String name, boolean includeAltNames) {
        name = ElementUtils.capitalise(name);
        LOGGER.info("Finding item with name: {}", name);
        List<Item> nameList = StreamSupport.stream(this.getItems()
            .find(Filters.in("name", name)).spliterator(), false)
            .collect(Collectors.toList());
        if (includeAltNames) {
            List<Item> altNameList = StreamSupport.stream(this.getItems()
                .find(Filters.in("alternative_names", name)).spliterator(), false)
                .collect(Collectors.toList());
            nameList.addAll(altNameList);
        }
        return nameList;
    }

    public List<Item> findItemsByKeyword(String keyword) {
        LOGGER.info("Finding items with keyword: {}", keyword);
        return StreamSupport.stream(this.getItems()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Magic School Functions
    public MagicSchool getMagicSchoolByUUID(UUID uuid) {
        return this.getMagicSchools()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<MagicSchool> findMagicSchoolsBySource(UUID sourceUUID) {
        return StreamSupport.stream(this.getMagicSchools()
            .find(Filters.eq("source_uuid", sourceUUID)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<MagicSchool> findMagicSchoolsByName(String name) {
        return StreamSupport.stream(this.getMagicSchools()
            .find(Filters.in("name", name)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<MagicSchool> findMagicSchoolsByKeyword(String keyword) {
        return StreamSupport.stream(this.getMagicSchools()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Spell Functions
    public Spell getSpellByUUID(UUID uuid) {
        return this.getSpells()
            .find(Filters.eq("_id", uuid))
            .first();
    }
    //endregion

    //region Creator Functions
    public Creator getCreatorByUUID(String id) {
        return this.getCreators()
            .find(Filters.eq("_id", id))
            .first();
    }
    //endregion

    //region Source Functions
    public Source getSourceByUUID(UUID uuid) {
        return this.getSources()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Source> findSourcesByKeyword(String keyword) {
        return StreamSupport.stream(this.getSources()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Source> findSourcesByOwnerID(String ownerID) {
        return StreamSupport.stream(this.getSources()
            .find(Filters.eq("owner_id", ownerID)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Source> findSourcesByMemberID(String memberID) {
        return StreamSupport.stream(this.getSources()
            .find(Filters.in("discord_ids", memberID)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Database Functions
    public MongoClient getClient() {
        return this.client;
    }

    public MongoDatabase getDatabase() {
        return this.getClient().getDatabase("compendium").withCodecRegistry(this.codecRegistry);
    }

    public MongoCollection<Alignment> getAlignments() {
        return this.getDatabase().getCollection("alignments", Alignment.class);
    }

    public void storeAlignment(Alignment alignment) {
        if (this.getAlignmentByUUID(alignment.getUUID()) == null) {
            this.getAlignments().insertOne(alignment);
        } else {
            this.getAlignments().findOneAndReplace(Filters.eq("_id", alignment.getUUID()), alignment);
        }
    }

    public void storeAlignments(Alignment... alignments) {
        for (Alignment alignment : alignments) {
            this.storeAlignment(alignment);
        }
    }

    public MongoCollection<Background> getBackgrounds() {
        return this.getDatabase().getCollection("backgrounds", Background.class);
    }

    public void storeBackground(Background background) {
        if (this.getBackgroundByUUID(background.getUUID()) == null) {
            this.getBackgrounds().insertOne(background);
        } else {
            this.getBackgrounds().findOneAndReplace(Filters.eq("_id", background.getUUID()), background);
        }
    }

    public void storeBackgrounds(Background... backgrounds) {
        for (Background background : backgrounds) {
            this.storeBackground(background);
        }
    }

    public MongoCollection<CharacterClass> getClasses() {
        return this.getDatabase().getCollection("classes", CharacterClass.class);
    }

    public void storeClass(CharacterClass characterClass) {
        if (this.getClassByUUID(characterClass.getUUID()) == null) {
            this.getClasses().insertOne(characterClass);
        } else {
            this.getClasses().findOneAndReplace(Filters.eq("_id", characterClass.getUUID()), characterClass);
        }
    }

    public void storeClasses(CharacterClass... classes) {
        for (CharacterClass characterClass : classes) {
            this.storeClass(characterClass);
        }
    }

    public MongoCollection<Feature> getFeatures() {
        return this.getDatabase().getCollection("features", Feature.class);
    }

    public void storeFeature(Feature feature) {
        if (this.getFeatureByUUID(feature.getUUID()) == null) {
            this.getFeatures().insertOne(feature);
        } else {
            this.getFeatures().findOneAndReplace(Filters.eq("_id", feature.getUUID()), feature);
        }
    }

    public void storeFeatures(Feature... features) {
        for (Feature feature : features) {
            this.storeFeature(feature);
        }
    }

    public MongoCollection<Subclass> getSubclasses() {
        return this.getDatabase().getCollection("subclasses", Subclass.class);
    }

    public void storeSubclass(Subclass subclass) {
        if (this.getSubclassByUUID(subclass.getUUID()) == null) {
            this.getSubclasses().insertOne(subclass);
        } else {
            this.getSubclasses().findOneAndReplace(Filters.eq("_id", subclass.getUUID()), subclass);
        }
    }

    public void storeSubclasses(Subclass... subclasses) {
        for (Subclass subclass : subclasses) {
            this.storeSubclass(subclass);
        }
    }

    public MongoCollection<Feat> getFeats() {
        return this.getDatabase().getCollection("feats", Feat.class);
    }

    public void storeFeat(Feat feat) {
        if (this.getFeatByUUID(feat.getUUID()) == null) {
            this.getFeats().insertOne(feat);
        } else {
            this.getFeats().findOneAndReplace(Filters.eq("_id", feat.getUUID()), feat);
        }
    }

    public void storeFeats(Feat... feats) {
        for (Feat feat : feats) {
            this.storeFeat(feat);
        }
    }

    public MongoCollection<Language> getLanguages() {
        return this.getDatabase().getCollection("languages", Language.class);
    }

    public void storeLanguage(Language language) {
        if (this.getLanguageByUUID(language.getUUID()) == null) {
            this.getLanguages().insertOne(language);
        } else {
            this.getLanguages().findOneAndReplace(Filters.eq("_id", language.getUUID()), language);
        }
    }

    public void storeLanguages(Language... languages) {
        for (Language language : languages) {
            this.storeLanguage(language);
        }
    }

    public MongoCollection<Proficiency> getProficiencies() {
        return this.getDatabase().getCollection("proficiencies", Proficiency.class);
    }

    public void storeProficiency(Proficiency proficiency) {
        if (this.getProficiencyByUUID(proficiency.getUUID()) == null) {
            this.getProficiencies().insertOne(proficiency);
        } else {
            this.getProficiencies().findOneAndReplace(Filters.eq("_id", proficiency.getUUID()), proficiency);
        }
    }

    public void storeProficiencies(Proficiency... proficiencies) {
        for (Proficiency proficiency : proficiencies) {
            this.storeProficiency(proficiency);
        }
    }

    public MongoCollection<Race> getRaces() {
        return this.getDatabase().getCollection("races", Race.class);
    }

    public void storeRace(Race race) {
        if (this.getRaceByUUID(race.getUUID()) == null) {
            this.getRaces().insertOne(race);
        } else {
            this.getRaces().findOneAndReplace(Filters.eq("_id", race.getUUID()), race);
        }
    }

    public void storeRaces(Race... races) {
        for (Race race : races) {
            this.storeRace(race);
        }
    }

    public MongoCollection<Category> getCategories() {
        return this.getDatabase().getCollection("categories", Category.class);
    }

    public void storeCategory(Category category) {
        if (this.getCategoryByUUID(category.getUUID()) == null) {
            this.getCategories().insertOne(category);
        } else {
            this.getCategories().findOneAndReplace(Filters.eq("_id", category.getUUID()), category);
        }
    }

    public void storeCategories(Category... categories) {
        for (Category category : categories) {
            this.storeCategory(category);
        }
    }

    public MongoCollection<Currency> getCurrencies() {
        return this.getDatabase().getCollection("currencies", Currency.class);
    }

    public void storeCurrency(Currency currency) {
        if (this.getCurrencyByUUID(currency.getUUID()) == null) {
            this.getCurrencies().insertOne(currency);
        } else {
            this.getCurrencies().findOneAndReplace(Filters.eq("_id", currency.getUUID()), currency);
        }
    }

    public void storeCurrencies(Currency... currencies) {
        for (Currency currency : currencies) {
            this.storeCurrency(currency);
        }
    }

    public MongoCollection<Item> getItems() {
        return this.getDatabase().getCollection("items", Item.class);
    }

    public void storeItem(Item item) {
        if (this.getItemByUUID(item.getUUID()) == null) {
            this.getItems().insertOne(item);
        } else {
            this.getItems().findOneAndReplace(Filters.eq("_id", item.getUUID()), item);
        }
    }

    public void storeItems(Item... items) {
        for (Item item : items) {
            this.storeItem(item);
        }
    }

    public MongoCollection<MagicSchool> getMagicSchools() {
        return this.getDatabase().getCollection("magic_schools", MagicSchool.class);
    }

    public void storeMagicSchool(MagicSchool magicSchool) {
        if (this.getMagicSchoolByUUID(magicSchool.getUUID()) == null) {
            this.getMagicSchools().insertOne(magicSchool);
        } else {
            this.getMagicSchools().findOneAndReplace(Filters.eq("_id", magicSchool.getUUID()), magicSchool);
        }
    }

    public void storeMagicSchools(MagicSchool... magicSchools) {
        for (MagicSchool magicSchool : magicSchools) {
            this.storeMagicSchool(magicSchool);
        }
    }

    public MongoCollection<Spell> getSpells() {
        return this.getDatabase().getCollection("spells", Spell.class);
    }

    public void storeSpell(Spell spell) {
        if (this.getSpellByUUID(spell.getUUID()) == null) {
            this.getSpells().insertOne(spell);
        } else {
            this.getSpells().findOneAndReplace(Filters.eq("_id", spell.getUUID()), spell);
        }
    }

    public void storeSpells(Spell... spells) {
        for (Spell spell : spells) {
            this.storeSpell(spell);
        }
    }

    public MongoCollection<Creator> getCreators() {
        return this.getDatabase().getCollection("creators", Creator.class);
    }

    public void storeCreator(Creator creator) {
        if (this.getCreatorByUUID(creator.getDiscordId()) == null) {
            this.getCreators().insertOne(creator);
        } else {
            this.getCreators().findOneAndReplace(Filters.eq("_id", creator.getDiscordId()), creator);
        }
    }

    public MongoCollection<Source> getSources() {
        return this.getDatabase().getCollection("sources", Source.class);
    }

    public void storeSource(Source source) {
        if (this.getSourceByUUID(source.getUUID()) == null) {
            this.getSources().insertOne(source);
        } else {
            this.getSources().findOneAndReplace(Filters.eq("_id", source.getUUID()), source);
        }
    }

    public void storeSources(Source... sources) {
        for (Source source : sources) {
            this.storeSource(source);
        }
    }
    //endregion

    //region UUID Validation Functions
    public UUID createAlignmentUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getAlignmentByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createAlignmentUUID();
        }
    }

    public UUID createBackgroundUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getBackgroundByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createBackgroundUUID();
        }
    }

    public UUID createClassUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getClassByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createClassUUID();
        }
    }

    public UUID createFeatureUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getFeatureByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createFeatureUUID();
        }
    }

    public UUID createSubclassUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getSubclassByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createSubclassUUID();
        }
    }

    public UUID createFeatUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getFeatByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createFeatUUID();
        }
    }

    public UUID createLanguageUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getLanguageByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createLanguageUUID();
        }
    }

    public UUID createProficiencyUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getProficiencyByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createProficiencyUUID();
        }
    }

    public UUID createRaceUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getRaceByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createRaceUUID();
        }
    }

    public UUID createSourceUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getSourceByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createSourceUUID();
        }
    }

    public UUID createMagicSchoolUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getMagicSchoolByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createMagicSchoolUUID();
        }
    }

    public UUID createCurrencyUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getCurrencyByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createCurrencyUUID();
        }
    }

    public UUID createItemUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getItemByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createItemUUID();
        }
    }

    public UUID createSpellUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getSpellByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createSpellUUID();
        }
    }

    public UUID createCategoryUUID() {
        UUID uuid = UUID.randomUUID();
        if (this.getCategoryByUUID(uuid) == null) {
            return uuid;
        } else {
            return this.createCategoryUUID();
        }
    }
    //endregion
}
