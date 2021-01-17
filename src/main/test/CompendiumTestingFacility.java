import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.Alignment;
import dev.compendium.core.character.component.Background;
import dev.compendium.core.character.component.Feature;
import dev.compendium.core.character.component.Language;
import dev.compendium.core.character.component.LanguageType;
import dev.compendium.core.character.component.Proficiency;
import dev.compendium.core.item.Category;
import dev.compendium.core.item.Currency;
import dev.compendium.core.item.Item;
import dev.compendium.core.spell.MagicSchool;
import dev.compendium.core.util.CharacterUtils;
import dev.compendium.core.util.Choice;
import dev.compendium.core.util.ElementUtils;
import dev.compendium.core.util.Metadata;
import dev.compendium.core.util.Source;
import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.bson.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompendiumTestingFacility {

    private static final Logger LOGGER = LoggerFactory.getLogger("CTF");

    public static void main(String[] args) {
        long totalTime = System.currentTimeMillis();
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Initialising element registry...");
        long time = System.currentTimeMillis();
        ElementRegistry.getInstance();
        LOGGER.info("Element registry initialised. Time taken: {}ms", System.currentTimeMillis() - time);
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Loading SRD source...");
        time = System.currentTimeMillis();
        Source source = ElementRegistry.getInstance().findSourcesByOwnerID("-1").get(0);
        LOGGER.info("Source Name: {}", source.getName());
        LOGGER.info("Source UUID: {}", source.getUUID());
        LOGGER.info("Loading complete. Time taken: {}ms", System.currentTimeMillis() - time);
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Creating Acolyte background...");
        Background acolyte = new Background(source, "Acolyte");
        acolyte.setDescription("You have spent your life in the service of a temple to a specific god or pantheon of "
            + "gods. You act as an intermediary between the realm of the holy and the mortal world, performing sacred "
            + "rites and offering sacrifices in order to conduct worshipers into the presence of the divine. You are "
            + "not necessarily a cleric - performing sacred rites is not the same thing as channeling divine power.\n\n"
            + "Choose a god, a pantheon of gods, or some other quasi-divine being from among those listed in "
            + "\"Fantasy--Historical Pantheons\" or those specified by your GM, and work with your GM to detail the "
            + "nature of your religious service. Were you a lesser functionary in a temple, raised from childhood to "
            + "assist the priests in the sacred rites? Or were you a high priest who suddenly experienced a call to "
            + "serve your god in a different way? Perhaps you were the leader of a small cult outside of any "
            + "established temple structure, or even an occult group that served a fiendish master that you now deny.");
        Proficiency insightSkill = ElementRegistry.getInstance().findProficienciesByKeyword("insight").get(0);
        Proficiency religionSkill = ElementRegistry.getInstance().findProficienciesByKeyword("religion").get(0);
        Choice languageChoices = new Choice("language", 2, ElementRegistry.getInstance().getAllLanguageUUIDs().toArray());
        Item amulet = ElementRegistry.getInstance().findItemsByName("Amulet").get(0);
        Item emblem = ElementRegistry.getInstance().findItemsByName("Emblem").get(0);
        Item reliquary = ElementRegistry.getInstance().findItemsByName("Reliquary").get(0);
        Choice holySymbolChoices = new Choice("item", 1, amulet.getUUID(), emblem.getUUID(), reliquary.getUUID());
        Item commonClothes = ElementRegistry.getInstance().findItemsByName("Clothes, Common").get(0);
        Item pouch = ElementRegistry.getInstance().findItemsByName("Pouch").get(0);
        Metadata bonusGold = new Metadata();
        bonusGold.set("gp", 15);
        Feature shelterOfTheFaithful = new Feature(source, "Shelter of the Faithful");
        shelterOfTheFaithful.setDescription("As an acolyte, you command the respect of those who share your faith, and "
            + "you can perform the religious ceremonies of your deity. You and your adventuring companions can expect "
            + "to receive free healing and care at a temple, shrine, or other established presence of your faith, "
            + "though you must provide any material components needed for spells. Those who share your religion will "
            + "support you (but only you) at a modest lifestyle.\n\nYou might also have ties to a specific temple "
            + "dedicated to your chosen deity or pantheon, and you have a residence there. This could be the temple "
            + "where you used to serve, if you remain on good terms with it, or a temple where you have found a new "
            + "home. While near your temple, you can call upon the priests for assistance, provided the assistance you "
            + "ask for is not hazardous and you remain in good standing with your temple.");
        ElementRegistry.getInstance().storeFeature(shelterOfTheFaithful);
        acolyte.getMetadata().set("choices", Arrays.asList(languageChoices, holySymbolChoices));
        acolyte.getMetadata().set("bonus", bonusGold.getDocument());
        acolyte.addEquipment(commonClothes, 1);
        acolyte.addEquipment(pouch, 1);
        acolyte.addProficiency(insightSkill);
        acolyte.addProficiency(religionSkill);
        acolyte.addFeature(shelterOfTheFaithful);
        acolyte.addSuggestedTrait("I idolize a particular hero of my faith, and constantly refer to that person's deeds and example.");
        acolyte.addSuggestedTrait("I can find common ground between the fiercest enemies, empathizing with them and always working toward peace.");
        acolyte.addSuggestedTrait("I see omens in every event and action. The gods try to speak to us, we just need to listen");
        acolyte.addSuggestedTrait("Nothing can shake my optimistic attitude.");
        acolyte.addSuggestedTrait("I quote (or misquote) sacred texts and proverbs in almost every situation.");
        acolyte.addSuggestedTrait("I am tolerant (or intolerant) of other faiths and respect (or condemn) the worship of other gods.");
        acolyte.addSuggestedTrait("I've enjoyed fine food, drink, and high society among my temple's elite. Rough living grates on me.");
        acolyte.addSuggestedTrait("I've spent so long in the temple that I have little practical experience dealing with people in the outside world.");
        acolyte.addSuggestedIdeal("Tradition. The ancient traditions of worship and sacrifice must be preserved and upheld. (Lawful)");
        acolyte.addSuggestedIdeal("Charity. I always try to help those in need, no matter what the personal cost. (Good)");
        acolyte.addSuggestedIdeal("Change. We must help bring about the changes the gods are constantly working in the world. (Chaotic)");
        acolyte.addSuggestedIdeal("Power. I hope to one day rise to the top of my faith's religious hierarchy. (Lawful)");
        acolyte.addSuggestedIdeal("Faith. I trust that my deity will guide my actions. I have faith that if I work hard, things will go well. (Lawful)");
        acolyte.addSuggestedIdeal("Aspiration. I seek to prove myself worthy of my god's favor by matching my actions against his or her teachings. (Any)");
        acolyte.addSuggestedBond("I would die to recover an ancient relic of my faith that was lost long ago.");
        acolyte.addSuggestedBond("I will someday get revenge on the corrupt temple hierarchy who branded me a heretic.");
        acolyte.addSuggestedBond("I owe my life to the priest who took me in when my parents died.");
        acolyte.addSuggestedBond("Everything I do is for the common people.");
        acolyte.addSuggestedBond("I will do anything to protect the temple where I served.");
        acolyte.addSuggestedBond("I seek to preserve a sacred text that my enemies consider heretical and seek to destroy.");
        acolyte.addSuggestedFlaw("I judge others harshly, and myself even more severely.");
        acolyte.addSuggestedFlaw("I put too much trust in those who wield power within my temple's hierarchy.");
        acolyte.addSuggestedFlaw("My piety sometimes leads me to blindly trust those that profess faith in my god.");
        acolyte.addSuggestedFlaw("I am inflexible in my thinking.");
        acolyte.addSuggestedFlaw("I am suspicious of strangers and expect the worst of them.");
        acolyte.addSuggestedFlaw("Once I pick a goal, I become obsessed with it to the detriment of everything else in my life.");
        LOGGER.info("Printing background conversion text...");
        LOGGER.info(ElementUtils.createBackgroundSummary(acolyte));

        LOGGER.info("Storage complete. Time taken: {}ms", System.currentTimeMillis() - time);
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Done.");
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Total time taken: {}ms", System.currentTimeMillis() - totalTime);
    }
}

/*
        LOGGER.info("Attempting to create Barbarian in code...");
        CharacterClass characterClass = new CharacterClass(source.getUUID(), "Barbarian");
        characterClass.setDescription("For some, their rage springs from a communion with fierce animal spirits. "
            + "Others draw from a roiling reservoir of anger at a world full of pain. For every barbarian, rage is a "
            + "power that fuels not just a battle frenzy but also uncanny reflexes, resilience, and feats of strength.");
        characterClass.setHitDice("1d12");

        Proficiency lightArmourProficiency = new Proficiency(source.getUUID(), "Light Armour");
        lightArmourProficiency.getMetadata().set("category", "armour");
        lightArmourProficiency.getMetadata().set("type", "light");
        characterClass.addProficiency(lightArmourProficiency);

        Proficiency mediumArmourProficiency = new Proficiency(source.getUUID(), "Medium Armour");
        mediumArmourProficiency.getMetadata().set("category", "armour");
        mediumArmourProficiency.getMetadata().set("type", "medium");
        characterClass.addProficiency(mediumArmourProficiency);

        Proficiency shieldProficiency = new Proficiency(source.getUUID(), "Shield");
        shieldProficiency.getMetadata().set("category", "armour");
        shieldProficiency.getMetadata().set("type", "shield");
        characterClass.addProficiency(shieldProficiency);

        Proficiency simpleWeaponProficiency = new Proficiency(source.getUUID(), "Simple Weapons");
        simpleWeaponProficiency.getMetadata().set("category", "weapons");
        simpleWeaponProficiency.getMetadata().set("type", "simple");
        characterClass.addProficiency(simpleWeaponProficiency);

        Proficiency martialWeaponProficiency = new Proficiency(source.getUUID(), "Martial Weapons");
        martialWeaponProficiency.getMetadata().set("category", "weapons");
        martialWeaponProficiency.getMetadata().set("type", "martial");
        characterClass.addProficiency(martialWeaponProficiency);

        Proficiency strengthSavingThrowProficiency = new Proficiency(source.getUUID(), "Strength");
        strengthSavingThrowProficiency.getMetadata().set("category", "saving_throw");
        strengthSavingThrowProficiency.getMetadata().set("type", "strength");
        characterClass.addProficiency(strengthSavingThrowProficiency);

        Proficiency constitutionSavingThrowProficiency = new Proficiency(source.getUUID(), "Constitution");
        constitutionSavingThrowProficiency.getMetadata().set("category", "saving_throw");
        constitutionSavingThrowProficiency.getMetadata().set("type", "constitution");
        characterClass.addProficiency(constitutionSavingThrowProficiency);

        Choice skillChoice = new Choice(2, "animal_handling", "athletics", "intimidation", "nature", "perception", "survival");
        characterClass.getMetadata().set("skill_choices", new Choice[]{skillChoice});
        */
