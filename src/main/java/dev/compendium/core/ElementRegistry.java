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
import dev.compendium.core.character.component.Language;
import dev.compendium.core.character.component.Proficiency;
import dev.compendium.core.character.component.Race;
import dev.compendium.core.item.Category;
import dev.compendium.core.item.CurrencyUnit;
import dev.compendium.core.item.Item;
import dev.compendium.core.spell.MagicSchool;
import dev.compendium.core.spell.Spell;
import dev.compendium.core.util.Source;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;
import org.bson.codecs.configuration.CodecRegistries;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.codecs.pojo.ClassModel;
import org.bson.codecs.pojo.PojoCodecProvider;

public class ElementRegistry {

    private static ElementRegistry instance;
    private final TextSearchOptions CASE_INSENSITIVE = new TextSearchOptions().caseSensitive(false)
        .diacriticSensitive(false);

    private final MongoClient client;
    private final CodecRegistry codecRegistry;

    //region Constructors and Instance Management Functions
    public ElementRegistry() {
        this.client = new MongoClient();
        this.codecRegistry = CodecRegistries.fromRegistries(MongoClient.getDefaultCodecRegistry(),
            CodecRegistries.fromProviders(PojoCodecProvider.builder().automatic(true)
                .register(ClassModel.builder(Source.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(MagicSchool.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Spell.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Category.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(Item.class).enableDiscriminator(true).build())
                .register(ClassModel.builder(CurrencyUnit.class).enableDiscriminator(true).build())
                .build()));
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

    public List<Background> findBackgroundsByKeyword(String keyword) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByProficiency(UUID proficiency) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("proficiencies", proficiency)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByLanguage(UUID language) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("languages", language)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Background> findBackgroundsByEquipment(UUID item) {
        return StreamSupport.stream(this.getBackgrounds()
            .find(Filters.in("equipment", item)).spliterator(), false)
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

    //region Feat Functions
    public Feat getFeatByUUID(UUID uuid) {
        return this.getFeats()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<Feat> findFeatsByKeyword(String keyword) {
        return StreamSupport.stream(this.getFeats()
            .find(Filters.text(keyword, CASE_INSENSITIVE)).spliterator(), false)
            .collect(Collectors.toList());
    }
    //endregion

    //region Language Functions
    public Language getLanguageByUUID(UUID uuid) {
        return this.getLanguages()
            .find(Filters.eq("_id", uuid))
            .first();
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
    public CurrencyUnit getCurrencyByUUID(UUID uuid) {
        return this.getCurrencies()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<CurrencyUnit> findCurrencyUnitsByKeyword(String name) {
        return StreamSupport.stream(this.getCurrencies()
            .find(Filters.text(name, CASE_INSENSITIVE)).spliterator(), false)
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
    //endregion

    //region Magic School Functions
    public MagicSchool getMagicSchoolByUUID(UUID uuid) {
        return this.getMagicSchools()
            .find(Filters.eq("_id", uuid))
            .first();
    }

    public List<MagicSchool> findMagicSchoolsBySource(Source source) {
        return StreamSupport.stream(this.getMagicSchools()
            .find(Filters.eq("source", source)).spliterator(), false)
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
            .find(Filters.eq("ownerID", ownerID)).spliterator(), false)
            .collect(Collectors.toList());
    }

    public List<Source> findSourcesByMemberID(String memberID) {
        return StreamSupport.stream(this.getSources()
            .find(Filters.in("discordIDs", memberID)).spliterator(), false)
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

    public MongoCollection<Background> getBackgrounds() {
        return this.getDatabase().getCollection("backgrounds", Background.class);
    }

    public MongoCollection<CharacterClass> getClasses() {
        return this.getDatabase().getCollection("classes", CharacterClass.class);
    }

    public MongoCollection<Feat> getFeats() {
        return this.getDatabase().getCollection("feats", Feat.class);
    }

    public MongoCollection<Language> getLanguages() {
        return this.getDatabase().getCollection("languages", Language.class);
    }

    public MongoCollection<Proficiency> getProficiencies() {
        return this.getDatabase().getCollection("proficiencies", Proficiency.class);
    }

    public MongoCollection<Race> getRaces() {
        return this.getDatabase().getCollection("races", Race.class);
    }

    public MongoCollection<Category> getCategories() {
        return this.getDatabase().getCollection("categories", Category.class);
    }

    public MongoCollection<CurrencyUnit> getCurrencies() {
        return this.getDatabase().getCollection("currencies", CurrencyUnit.class);
    }

    public MongoCollection<Item> getItems() {
        return this.getDatabase().getCollection("items", Item.class);
    }

    public MongoCollection<MagicSchool> getMagicSchools() {
        return this.getDatabase().getCollection("magicSchools", MagicSchool.class);
    }

    public MongoCollection<Spell> getSpells() {
        return this.getDatabase().getCollection("spells", Spell.class);
    }

    public MongoCollection<Source> getSources() {
        return this.getDatabase().getCollection("sources", Source.class);
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
