package dev.compendium.core.util;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.Background;
import dev.compendium.core.character.component.Feature;
import dev.compendium.core.character.component.Proficiency;
import dev.compendium.core.item.Currency;
import dev.compendium.core.item.Item;
import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bson.Document;

// TODO: write documentation
public class ElementUtils {

    public static String abbreviate(String input) {
        if (input.length() <= 1) {
            return input;
        }
        String[] chunks = input.split(" ");
        StringBuilder abbreviation = new StringBuilder();
        for (String chunk : chunks) {
            abbreviation.append(chunk.charAt(0));
        }
        return abbreviation.toString().trim();
    }

    public static String convertIDtoDisplay(String id) {
        StringBuilder sb = new StringBuilder();
        id = id.replaceAll("_", " ");
        for (String split : id.toLowerCase().split(" ")) {
            sb.append(split.substring(0, 1).toUpperCase()).append(split.substring(1)).append(" ");
        }
        return sb.toString().trim();
    }

    public static <T> List<T> matchingListFilter(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>();
        for (T t : a) {
            if (b.contains(t)) {
                result.add(t);
            }
        }
        return result;
    }

    public static <T> List<T> inverseMatchingListFilter(List<T> a, List<T> b) {
        List<T> result = new ArrayList<>(a);
        for (T t : b) {
            result.remove(t);
        }
        return result;
    }

    public static String createBackgroundSummary(Background background) {
        StringBuilder sb = new StringBuilder();
        // Title
        sb.append("**").append(background.getName()).append("**\n");
        // Description
        sb.append(background.getDescription()).append("\n\n");
        // Proficiencies
        List<Proficiency> skillProficiencies = new ArrayList<>();
        List<Proficiency> gamingSetProficiencies = new ArrayList<>();
        List<Proficiency> toolProficiencies = new ArrayList<>();
        List<Proficiency> musicalInstrumentProficiencies = new ArrayList<>();
        List<Proficiency> weaponProficiencies = new ArrayList<>();
        List<Proficiency> savingThrowProficiencies = new ArrayList<>();
        List<Proficiency> miscellaneousProficiencies = new ArrayList<>();
        for (Proficiency proficiency : background.getProficiencies()) {
            switch (proficiency.getMetadata().getDocument().get("category", String.class)) {
                case ("skill"):
                    skillProficiencies.add(proficiency);
                    break;
                case ("gaming_set"):
                    gamingSetProficiencies.add(proficiency);
                    break;
                case ("tool"):
                    toolProficiencies.add(proficiency);
                    break;
                case ("musical_instrument"):
                    musicalInstrumentProficiencies.add(proficiency);
                    break;
                case ("weapon"):
                    weaponProficiencies.add(proficiency);
                    break;
                case ("saving_throw"):
                    savingThrowProficiencies.add(proficiency);
                    break;
                default:
                    miscellaneousProficiencies.add(proficiency);
                    break;
            }
        }
        if (!skillProficiencies.isEmpty()) {
            sb.append("**Skill Proficiencies:** ");
            for (Proficiency proficiency : skillProficiencies) {
                sb.append(proficiency.getName());
                if (skillProficiencies.indexOf(proficiency) != skillProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!gamingSetProficiencies.isEmpty()) {
            sb.append("**Gaming Set Proficiencies:** ");
            for (Proficiency proficiency : gamingSetProficiencies) {
                sb.append(proficiency.getName());
                if (gamingSetProficiencies.indexOf(proficiency) != gamingSetProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!toolProficiencies.isEmpty()) {
            sb.append("**Tool Proficiencies:** ");
            for (Proficiency proficiency : toolProficiencies) {
                sb.append(proficiency.getName());
                if (toolProficiencies.indexOf(proficiency) != toolProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!musicalInstrumentProficiencies.isEmpty()) {
            sb.append("**Musical Instrument Proficiencies:** ");
            for (Proficiency proficiency : musicalInstrumentProficiencies) {
                sb.append(proficiency.getName());
                if (musicalInstrumentProficiencies.indexOf(proficiency) != musicalInstrumentProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!weaponProficiencies.isEmpty()) {
            sb.append("**Weapon Proficiencies:** ");
            for (Proficiency proficiency : weaponProficiencies) {
                sb.append(proficiency.getName());
                if (weaponProficiencies.indexOf(proficiency) != weaponProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!savingThrowProficiencies.isEmpty()) {
            sb.append("**Saving Throw Proficiencies:** ");
            for (Proficiency proficiency : savingThrowProficiencies) {
                sb.append(proficiency.getName());
                if (savingThrowProficiencies.indexOf(proficiency) != savingThrowProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        if (!miscellaneousProficiencies.isEmpty()) {
            sb.append("**Miscellaneous Proficiencies:** ");
            for (Proficiency proficiency : miscellaneousProficiencies) {
                sb.append(proficiency.getName());
                if (miscellaneousProficiencies.indexOf(proficiency) != miscellaneousProficiencies.size() - 1) {
                    sb.append(", ");
                }
            }
            sb.append("\n");
        }
        // Choices
        List<?> choices = background.getMetadata().getDocument().get("choices", List.class);
        if (!choices.isEmpty()) {
            sb.append("**Choices:**");
            for (Object obj : choices) {
                Choice choice = (Choice) obj;
                sb.append("\n- ").append(choice.getChoiceCount()).append(" ")
                    .append(convertIDtoDisplay(choice.getType())).append(choice.getChoiceCount() > 1 ? "s" : "")
                    .append(" from: ");
                List<Object> uuids = choice.getChoices();
                switch (choice.getType()) {
                    case ("language"):
                        for (Object uuid : uuids) {
                            sb.append(ElementRegistry.getInstance().getLanguageByUUID((UUID) uuid).getName());
                            if (choice.getChoices().indexOf(uuid) != choice.getChoices().size() - 1) {
                                sb.append(", ");
                            }
                        }
                        break;
                    case ("item"):
                        for (Object uuid : uuids) {
                            sb.append(ElementRegistry.getInstance().getItemByUUID((UUID) uuid).getName());
                            if (choice.getChoices().indexOf(uuid) != choice.getChoices().size() - 1) {
                                sb.append(", ");
                            }
                        }
                        break;
                    case ("skill"):
                    case ("saving_throw"):
                    case ("weapon"):
                    case ("tool"):
                    case ("musical_instrument"):
                        for (Object uuid : uuids) {
                            sb.append(ElementRegistry.getInstance().getProficiencyByUUID((UUID) uuid).getName());
                            if (choice.getChoices().indexOf(uuid) != choice.getChoices().size() - 1) {
                                sb.append(", ");
                            }
                        }
                        break;
                }
            }
            sb.append("\n");
        }
        // Equipment
        sb.append("**Equipment:** ");
        for (Map.Entry<Item, Integer> entry : background.getEquipment().entrySet()) {
            sb.append("\n- ").append(entry.getValue()).append("x ").append(entry.getKey().getName());
        }
        Document document = background.getMetadata().getDocument().get("bonus", Document.class);
        if (document != null) {
            for (Entry<String, Object> entry : document.entrySet()) {
                sb.append("\n- ").append(entry.getValue()).append(" ").append(entry.getKey());
            }
        }
        // Features
        for (Feature feature : background.getFeatures()) {
            sb.append("\n**").append(feature.getName());
            sb.append("**: ").append(feature.getDescription());
        }
        return sb.toString().trim();
    }

    public static EmbedBuilder createItemSummary(Item item) {
        EmbedBuilder result = new EmbedBuilder();
        result.setColor(randomColourGenerator());
        String title = item.getName();
        if (item.getQuantity() > 1) {
            title = title + " (" + item.getQuantity() + ")";
        }
        result.setTitle(title);
        if (item.getCategoryUUID() != null) {
            result.addField("Category", item.getCategory().getName(), true);
        }
        if (item.getCost() > 0) {
            Currency currency = item.getCurrency();
            System.out.println(currency.getAbbreviation());
            String costString = item.getCost() + currency.getAbbreviation();
            if (currency.getGoldPieceEquivalent() != 1) {
                costString = costString + " (" + (item.getCost() * currency.getGoldPieceEquivalent()) + "gp)";
            }
            result.addField("Cost", costString, true);
        }
        if (item.getWeight() > 0) {
            String weight;
            if (item.getWeight() != 1) {
                weight = processWeight(item.getWeight()) + " lbs.";
            } else {
                weight = processWeight(item.getWeight()) + " lb.";
            }
            result.addField("Weight", weight, true);
        }
        if (!item.getDescription().equals("")) {
            String description = "";
            if (item.getAdditionalTags().size() > 0) {
                description = "(" + String.join(", ", item.getAdditionalTags()) + ")\n";
            }
            description = description + item.getDescription();
            result.setDescription(description);
        }
        result.setFooter("Item Source - " + item.getSource().getAbbreviation());
        return result;
    }

    private static Color randomColourGenerator() {
        Random random = new Random();
        float hue = random.nextFloat();
        float saturation = 0.9f;
        float luminance = 1.0f;
        return Color.getHSBColor(hue, saturation, luminance);
    }

    private static String processWeight(double weight) {
        int intWeight = (int) weight;
        double decimalWeight = weight - intWeight;

        if (decimalWeight == 0) {
            return String.valueOf(intWeight);
        } else if (decimalWeight == 0.125) {
            return intWeight + " 1/8";
        } else if (decimalWeight == 0.25) {
            return intWeight + " 1/4";
        } else if (decimalWeight == 0.33) {
            return intWeight + " 1/3";
        } else if (decimalWeight == 0.375) {
            return intWeight + " 3/8";
        } else if (decimalWeight == 0.5) {
            return intWeight + " 1/2";
        } else if (decimalWeight == 0.625) {
            return intWeight + " 5/8";
        } else if (decimalWeight == 0.66) {
            return intWeight + " 2/3";
        } else if (decimalWeight == 0.75) {
            return intWeight + " 3/4";
        } else if (decimalWeight == 0.875) {
            return intWeight + " 7/8";
        }
        return "";
    }
}
