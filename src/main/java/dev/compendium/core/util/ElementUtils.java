package dev.compendium.core.util;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.character.component.Background;
import dev.compendium.core.character.component.Feature;
import dev.compendium.core.character.component.Proficiency;
import dev.compendium.core.item.Currency;
import dev.compendium.core.item.Item;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import org.bson.Document;

// TODO: write documentation
public class ElementUtils {

    // Source: https://stackoverflow.com/questions/3422673/how-to-evaluate-a-math-expression-given-in-string-form
    // yes, this is important
    public static double eval(final String str) {
        return new Object() {
            int pos = -1, ch;

            void nextChar() {
                ch = (++pos < str.length()) ? str.charAt(pos) : -1;
            }

            boolean eat(int charToEat) {
                while (ch == ' ')
                    nextChar();
                if (ch == charToEat) {
                    nextChar();
                    return true;
                }
                return false;
            }

            double parse() {
                nextChar();
                double x = parseExpression();
                if (pos < str.length())
                    throw new RuntimeException("Unexpected: " + (char) ch);
                return x;
            }

            // Grammar:
            // expression = term | expression `+` term | expression `-` term
            // term = factor | term `*` factor | term `/` factor
            // factor = `+` factor | `-` factor | `(` expression `)`
            //        | number | functionName factor | factor `^` factor

            double parseExpression() {
                double x = parseTerm();
                for (; ; ) {
                    if (eat('+'))
                        x += parseTerm(); // addition
                    else if (eat('-'))
                        x -= parseTerm(); // subtraction
                    else
                        return x;
                }
            }

            double parseTerm() {
                double x = parseFactor();
                for (; ; ) {
                    if (eat('*'))
                        x *= parseFactor(); // multiplication
                    else if (eat('/'))
                        x /= parseFactor(); // division
                    else
                        return x;
                }
            }

            double parseFactor() {
                if (eat('+'))
                    return parseFactor(); // unary plus
                if (eat('-'))
                    return -parseFactor(); // unary minus

                double x;
                int startPos = this.pos;
                if (eat('(')) { // parentheses
                    x = parseExpression();
                    eat(')');
                } else if ((ch >= '0' && ch <= '9') || ch == '.') { // numbers
                    while ((ch >= '0' && ch <= '9') || ch == '.')
                        nextChar();
                    x = Double.parseDouble(str.substring(startPos, this.pos));
                } else if (ch >= 'a' && ch <= 'z') { // functions
                    while (ch >= 'a' && ch <= 'z')
                        nextChar();
                    String func = str.substring(startPos, this.pos);
                    x = parseFactor();
                    switch (func) {
                        case "sqrt":
                            x = Math.sqrt(x);
                            break;
                        case "sin":
                            x = Math.sin(Math.toRadians(x));
                            break;
                        case "cos":
                            x = Math.cos(Math.toRadians(x));
                            break;
                        case "tan":
                            x = Math.tan(Math.toRadians(x));
                            break;
                        default:
                            throw new RuntimeException("Unknown function: " + func);
                    }
                } else {
                    throw new RuntimeException("Unexpected: " + (char) ch);
                }

                if (eat('^'))
                    x = Math.pow(x, parseFactor()); // exponentiation

                return x;
            }
        }.parse();
    }

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

    public static String capitalise(String input) {
        input = input.toLowerCase().replaceAll("_", " ");
        String modifiedInput = input.replaceAll("[^A-Za-z' ]", "");
        String[] words = modifiedInput.split(" ");
        for (String word : words) {
            input = input.replace(word, word.substring(0, 1).toUpperCase() + word.substring(1));
        }
        return input;
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

    @SuppressWarnings("unchecked")
    public static EmbedBuilder createBackgroundSummary(Background background) {
        EmbedBuilder result = new EmbedBuilder();
        result.setColor(randomColourGenerator());
        // Background Name
        result.setTitle(background.getName());
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
            result.addField("Skill Proficiencies",
                skillProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!gamingSetProficiencies.isEmpty()) {
            result.addField("Gaming Set Proficiencies",
                gamingSetProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!toolProficiencies.isEmpty()) {
            result.addField("Tool Proficiencies",
                toolProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!musicalInstrumentProficiencies.isEmpty()) {
            result.addField("Musical Instrument Proficiencies",
                musicalInstrumentProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!weaponProficiencies.isEmpty()) {
            result.addField("Weapon Proficiencies",
                weaponProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!savingThrowProficiencies.isEmpty()) {
            result.addField("Saving Throw Proficiencies",
                savingThrowProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        if (!miscellaneousProficiencies.isEmpty()) {
            result.addField("Miscellaneous Proficiencies",
                miscellaneousProficiencies.stream().map(Proficiency::getName).collect(Collectors.joining(", ")), false);
        }
        // Equipment
        int iterator = 0;
        StringBuilder equipmentString = new StringBuilder();
        for (Map.Entry<Item, Integer> entry : background.getEquipment().entrySet()) {
            Item item = entry.getKey();
            String name = item.getName();
            if (name.contains(",")) {
                for (String altName : item.getAlternativeNames()) {
                    if (!altName.contains(",")) {
                        name = altName;
                        break;
                    }
                }
            }
            equipmentString.append(capitalise(name));
            if (entry.getValue() > 1) {
                equipmentString.append(" (").append(entry.getValue()).append(")");
            }
            if (iterator < background.getEquipment().size() - 1) {
                equipmentString.append(", ");
            }
            iterator++;
        }
        Document bonusDoc = background.getMetadata().getDocument().get("bonus", Document.class);
        if (bonusDoc != null) {
            for (Entry<String, Object> entry : bonusDoc.entrySet()) {
                equipmentString.append(", ").append(entry.getValue()).append(entry.getKey().toLowerCase());
            }
        }
        result.addField("Equipment", equipmentString.toString().trim(), false);
        // Choices
        List<Document> choices = background.getMetadata().getDocument().get("choices", List.class);
        if (!choices.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            for (Document choiceDoc : choices) {
                Choice choice = Choice.parse(choiceDoc);
                sb.append("\n- ").append(choice.getChoiceCount()).append(" ").append(capitalise(choice.getType()))
                    .append(choice.getChoiceCount() > 1 ? "s" : "").append(" from: ");
                List<String> choiceStrings = choice.getChoices();
                iterator = 0;
                switch (choice.getType()) {
                    case "language":
                        for (String str : choiceStrings) {
                            sb.append(capitalise(str.replaceAll("\\+", ", ")));
                            if (iterator < choiceStrings.size() - 1) {
                                sb.append(", ");
                            }
                            iterator++;
                        }
                        break;
                    case "item":
                        for (String str : choiceStrings) {
                            int quantity = 1;
                            if (str.contains("x")) {
                                String quantityCheck = str.substring(0, str.indexOf('x'));
                                if (quantityCheck.matches("[0-9]+x")) {
                                    quantity = Integer.parseInt(quantityCheck.replaceAll("[^0-9]", ""));
                                }
                            }
                            Item item = ElementRegistry.getInstance().getItemByUUID(UUID.fromString(str));
                            sb.append(capitalise(item.getName()));
                            if (quantity > 1) {
                                sb.append(" (").append(quantity).append(")");
                            }
                            if (iterator < choiceStrings.size() - 1) {
                                sb.append(", ");
                            }
                            iterator++;
                        }
                        break;
                    case "skill":
                    case "saving_throw":
                    case "weapon":
                    case "tool":
                    case "musical_instrument":
                        for (String str : choiceStrings) {
                            Proficiency proficiency = ElementRegistry.getInstance().getProficiencyByUUID(UUID.fromString(str));
                            sb.append(capitalise(proficiency.getName()));
                            if (iterator < choiceStrings.size() - 1) {
                                sb.append(", ");
                            }
                            iterator++;
                        }
                        break;
                }
            }
            result.addField("Choices", sb.toString().trim(), false);
        }
        // Description
        result.addField("Description", background.getDescription(), false);
        for (Feature feature : background.getFeatures()) {
            result.addField("Feature - " + feature.getName(), feature.getDescription(), false);
        }
        result.setFooter("Background Source - " + background.getSource().getAbbreviation());
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
