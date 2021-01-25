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
import javax.swing.text.Element;
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
        // stuff
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Storing...");
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
