package dev.compendium.bot.utils;

import dev.compendium.core.util.ElementUtils;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.internal.utils.tuple.Pair;
import uk.co.binaryoverload.dicerollparser.Parser;
import uk.co.binaryoverload.dicerollparser.enums.Operator;
import uk.co.binaryoverload.dicerollparser.objects.AddedValue;
import uk.co.binaryoverload.dicerollparser.objects.DiceRoll;
import uk.co.binaryoverload.dicerollparser.objects.Die;
import uk.co.binaryoverload.dicerollparser.objects.Modifier;

@SuppressWarnings({"DuplicatedCode", "unchecked"})
public class RollUtils {

    public static List<RollResults> processRoll(User sender, Message message, MessageChannel channel, String command,
        String rollString, long iterations) {
        List<DiceRoll> rolls = Parser.parseDiceRoll(rollString);
        StringBuilder parsedSection = new StringBuilder();
        for (DiceRoll roll : rolls) {
            parsedSection.append(createDieString(roll));
        }
        String remainder = rollString.replace(parsedSection.toString(), "");
        List<RollResults> results = new ArrayList<>();
        for (int i = 0; i < iterations; i++) {
            StringBuilder sb = new StringBuilder();
            if (rolls.size() > 0) {
                Map<DiceRoll, Map<Die, List<Long>>> finalDiceRolls = rollDice(rolls, channel);
                if (finalDiceRolls == null) {
                    return null;
                }
                RollResults res = assembleRoll(finalDiceRolls, sb, remainder);
                results.add(res);
            } else {
                double eval = ElementUtils.eval(rollString);
                sb.append(rollString);
                RollResults res = new RollResults(null, null, (long) Math.floor(eval), eval, sb);
                results.add(res);
            }
        }
        return results;
    }

    private static Map<DiceRoll, Map<Die, List<Long>>> rollDice(List<DiceRoll> rolls, MessageChannel channel) {
        Map<DiceRoll, Map<Die, List<Long>>> finalRollValues = new LinkedHashMap<>();
        for (DiceRoll roll : rolls) {
            Map<Die, List<Long>> diceRollValues = new LinkedHashMap<>();
            for (Die die : roll.getDice()) {
                List<Long> rollValues = new ArrayList<>();
                if (die.getDiceCount() <= 0) {
                    sendErrorMessage(channel, "Invalid roll: Cannot have 0 dice "
                        + "(`" + die.getDiceCount() + "d" + die.getDiceValue() + "`)");
                    break;
                }
                for (int i = 0; i < die.getDiceCount(); i++) {
                    long rollValue = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                    rollValues.add(rollValue);
                }
                diceRollValues.put(die, rollValues);
            }
            Pair<Boolean, Object> modifierResult = processModifiers(diceRollValues, roll);
            if (modifierResult.getLeft()) {
                diceRollValues = (Map<Die, List<Long>>) modifierResult.getRight();
            } else {
                sendErrorMessage(channel, "Roll failed while applying modifiers: "
                    + modifierResult.getRight());
                return null;
            }
            finalRollValues.put(roll, diceRollValues);
        }
        return finalRollValues;
    }

    private static RollResults assembleRoll(Map<DiceRoll, Map<Die, List<Long>>> finalRollValues, StringBuilder sb,
        String remainder) {
        Map<DiceRoll, Long> finalRolls = new LinkedHashMap<>();
        for (Map.Entry<DiceRoll, Map<Die, List<Long>>> entry : finalRollValues.entrySet()) {
            long finalRoll = 0;
            DiceRoll roll = entry.getKey();
            Map<Die, List<Long>> rollResults = entry.getValue();
            if (roll.getDice().size() > 1) {
                sb.append("(");
                for (Die die : roll.getDice()) {
                    List<Long> dieResults = rollResults.get(die);
                    int indicator = 1;
                    sb.append(die.getDiceCount()).append("d").append(die.getDiceValue()).append(" (");
                    for (long result : dieResults) {
                        finalRoll += result;
                        if (result == 1 || result == die.getDiceValue()) {
                            sb.append("**").append(result).append("**");
                        } else {
                            sb.append(result);
                        }
                        if (indicator != dieResults.size()) {
                            sb.append(", ");
                        }
                        indicator++;
                    }
                    sb.append(")");
                    if (roll.getDice().indexOf(die) != roll.getDice().size() - 1) {
                        sb.append(", ");
                    }
                }
                sb.append(")").append(createModifierString(roll));
            } else {
                Die die = roll.getDice().get(0);
                List<Long> dieResults = rollResults.get(die);
                sb.append(die.getDiceCount()).append("d").append(die.getDiceValue())
                    .append(createModifierString(roll)).append(" (");
                int indicator = 1;
                for (long result : dieResults) {
                    finalRoll += result;
                    if (result == 1 || result == die.getDiceValue()) {
                        sb.append("**").append(result).append("**");
                    } else {
                        sb.append(result);
                    }
                    if (indicator != dieResults.size()) {
                        sb.append(", ");
                    }
                    indicator++;
                }
                sb.append(")");
            }
            for (AddedValue addedValue : roll.getAddedValue()) {
                switch (addedValue.getOperator()) {
                    case ADD:
                        sb.append(" + ");
                        finalRoll += addedValue.getValue();
                        break;
                    case SUBTRACT:
                        sb.append(" - ");
                        finalRoll -= addedValue.getValue();
                        break;
                    case MULTIPLY:
                        sb.append(" \\* ");
                        finalRoll *= addedValue.getValue();
                        break;
                    case DIVIDE:
                        sb.append(" / ");
                        finalRoll /= addedValue.getValue();
                        break;
                }
                int value = addedValue.getValue().intValue();
                if (value == addedValue.getValue()) {
                    sb.append(value);
                } else {
                    sb.append(addedValue.getValue());
                }
            }
            if (roll.getLabel() != null) {
                sb.append(" [").append(roll.getLabel()).append("]");
            }
            if (roll.getOperator() != null) {
                switch (roll.getOperator()) {
                    case ADD:
                        sb.append(" + ");
                        break;
                    case SUBTRACT:
                        sb.append(" - ");
                        break;
                    case MULTIPLY:
                        sb.append(" \\* ");
                        break;
                    case DIVIDE:
                        sb.append(" / ");
                        break;
                }
            }
            finalRolls.put(roll, finalRoll);
        }
        Map<String, Long> labelledRolls = new LinkedHashMap<>();
        long theActualFinalResult = 0;
        Operator nextOperator = null;
        for (Map.Entry<DiceRoll, Long> finalRoll : finalRolls.entrySet()) {
            String label = finalRoll.getKey().getLabel();
            if (label != null) {
                if (labelledRolls.containsKey(label)) {
                    long value = labelledRolls.get(label);
                    labelledRolls.put(label, value + finalRoll.getValue());
                } else {
                    labelledRolls.put(label, finalRoll.getValue());
                }
            }
            if (nextOperator == null) {
                theActualFinalResult += finalRoll.getValue();
            } else {
                switch (nextOperator) {
                    case ADD:
                        theActualFinalResult += finalRoll.getValue();
                        break;
                    case SUBTRACT:
                        theActualFinalResult -= finalRoll.getValue();
                        break;
                    case MULTIPLY:
                        theActualFinalResult *= finalRoll.getValue();
                        break;
                    case DIVIDE:
                        theActualFinalResult /= finalRoll.getValue();
                        break;
                }
            }
            nextOperator = finalRoll.getKey().getOperator();
        }
        if (!remainder.equals("")) {
            if (remainder.contains("[")) {
                try {
                    String val = remainder.substring(0, remainder.indexOf('['));
                    String label = remainder.substring(remainder.indexOf('[') + 1).replaceAll("]", "");
                    long valAsLong = (long) ElementUtils.eval(val);
                    if (nextOperator != null) {
                        switch (nextOperator) {
                            case ADD:
                                theActualFinalResult += valAsLong;
                                break;
                            case SUBTRACT:
                                theActualFinalResult -= valAsLong;
                                break;
                            case MULTIPLY:
                                theActualFinalResult *= valAsLong;
                                break;
                            case DIVIDE:
                                theActualFinalResult /= valAsLong;
                                break;
                        }
                    } else {
                        theActualFinalResult += valAsLong;
                    }
                    labelledRolls.put(label, valAsLong);
                    sb.append(valAsLong).append(" [").append(label).append("]");
                } catch (NumberFormatException ignored) {
                }
            } else {
                long valAsLong = (long) ElementUtils.eval(remainder);
                theActualFinalResult += valAsLong;
            }
        }
        return new RollResults(finalRolls, labelledRolls, theActualFinalResult, theActualFinalResult, sb);
    }

    private static String createDieString(DiceRoll roll) {
        StringBuilder sb = new StringBuilder();
        if (roll.getDice().size() > 1) {
            sb.append("(");
            for (Die die : roll.getDice()) {
                sb.append(die.getDiceCount()).append("d").append(die.getDiceValue());
                if (roll.getDice().indexOf(die) != roll.getDice().size() - 1) {
                    sb.append(",");
                }
            }
            sb.append(")");
        } else {
            Die die = roll.getDice().get(0);
            sb.append(die.getDiceCount()).append("d").append(die.getDiceValue());
        }
        sb.append(createModifierString(roll));
        for (AddedValue addedValue : roll.getAddedValue()) {
            switch (addedValue.getOperator()) {
                case ADD:
                    sb.append("+");
                    break;
                case SUBTRACT:
                    sb.append("-");
                    break;
                case MULTIPLY:
                    sb.append("*");
                    break;
                case DIVIDE:
                    sb.append("/");
                    break;
            }
            int value = addedValue.getValue().intValue();
            if (value == addedValue.getValue()) {
                sb.append(value);
            } else {
                sb.append(addedValue.getValue());
            }
        }
        if (roll.getLabel() != null) {
            sb.append("[").append(roll.getLabel()).append("]");
        }
        if (roll.getOperator() != null) {
            switch (roll.getOperator()) {
                case ADD:
                    sb.append("+");
                    break;
                case SUBTRACT:
                    sb.append("-");
                    break;
                case MULTIPLY:
                    sb.append("*");
                    break;
                case DIVIDE:
                    sb.append("/");
                    break;
            }
        }
        return sb.toString();
    }

    private static String createModifierString(DiceRoll roll) {
        StringBuilder sb = new StringBuilder();
        for (Modifier modifier : roll.getModifiers()) {
            switch (modifier.getType()) {
                case REROLL:
                    sb.append("rr");
                    break;
                case REROLL_ONE:
                    sb.append("ro");
                    break;
                case REROLL_ADD:
                    sb.append("ra");
                    break;
                case MINIMUM:
                    sb.append("mi");
                    break;
                case MAXIMUM:
                    sb.append("ma");
                    break;
                case EXPLODE:
                    sb.append("e");
                    break;
                case KEEP:
                    sb.append("k");
                    break;
                case DROP:
                    sb.append("d");
                    break;
            }
            if (modifier.getSelector() != null) {
                switch (modifier.getSelector()) {
                    case GREATER_THAN:
                        sb.append(">");
                        break;
                    case LESS_THAN:
                        sb.append("<");
                        break;
                    case LOW:
                        sb.append("l");
                        break;
                    case HIGH:
                        sb.append("h");
                        break;
                }
                int value = modifier.getSelectorValue().intValue();
                if (value == modifier.getSelectorValue()) {
                    sb.append(value);
                } else {
                    sb.append(modifier.getSelectorValue());
                }
            } else {
                sb.append(modifier.getValue());
            }
        }
        return sb.toString().trim();
    }

    private static void sendErrorMessage(MessageChannel channel, String error) {
        channel.sendMessage(new EmbedBuilder()
            .setColor(Color.decode("#990011"))
            .setTitle("Roll - Error")
            .setDescription(error)
            .build())
            .queue();
    }

    private static Pair<Boolean, Object> processModifiers(Map<Die, List<Long>> diceRollValues, DiceRoll roll) {
        for (Modifier modifier : roll.getModifiers()) {
            for (Map.Entry<Die, List<Long>> rollEntry : diceRollValues.entrySet()) {
                Die die = rollEntry.getKey();
                List<Long> rollResults = rollEntry.getValue();
                Map<Long, Long> rollIndexes = new ConcurrentHashMap<>();
                long index = 0;
                for (long rollResult : rollResults) {
                    rollIndexes.put(index, rollResult);
                    index++;
                }
                Pair<Boolean, Object> result = Pair.of(true, rollIndexes);
                switch (modifier.getType()) {
                    case REROLL:
                        result = processReroll(die, rollIndexes, modifier);
                        break;
                    case REROLL_ONE:
                        result = processRerollOnce(die, rollIndexes, modifier);
                        break;
                    case REROLL_ADD:
                        result = processRerollAdd(die, rollIndexes, modifier);
                        break;
                    case MINIMUM:
                        result = processMinimum(die, rollIndexes, modifier);
                        break;
                    case MAXIMUM:
                        result = processMaximum(die, rollIndexes, modifier);
                        break;
                    case EXPLODE:
                        result = processExplode(die, rollIndexes, modifier);
                        break;
                    case KEEP:
                        result = processKeep(die, rollIndexes, modifier);
                        break;
                    case DROP:
                        result = processDrop(die, rollIndexes, modifier);
                        break;
                }
                if (result.getLeft()) {
                    rollIndexes = (Map<Long, Long>) result.getRight();
                } else {
                    return result;
                }
                diceRollValues.replace(die, new ArrayList<>(rollIndexes.values()));
            }
        }
        return Pair.of(true, diceRollValues);
    }

    private static Pair<Boolean, Object> processReroll(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            do {
                rollIndexes.replaceAll((k, v) -> v = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
            } while (rollIndexes.containsValue(modifier.getValue().longValue()));
            return Pair.of(true, rollIndexes);
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair
                        .of(false, "Greater than resulting value cannot be equal to or higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() > modifier.getSelectorValue()) {
                        rollIndexes.replace(rollIndex.getKey(),
                            ThreadLocalRandom.current().nextLong(1, modifier.getSelectorValue().longValue() + 1));
                    }
                }
                return Pair.of(true, rollIndexes);
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() < modifier.getSelectorValue()) {
                        rollIndexes.replace(rollIndex.getKey(),
                            ThreadLocalRandom.current()
                                .nextLong(modifier.getSelectorValue().longValue(), die.getDiceValue() + 1));
                    }
                }
                return Pair.of(true, rollIndexes);
            case LOW:
                return Pair.of(false, "Lowest selector cannot be applied to infinite rerolls");
            case HIGH:
                return Pair.of(false, "Highest selector cannot be applied to infinite rerolls");
        }
        return Pair.of(true, "");
    }

    private static Pair<Boolean, Object> processRerollOnce(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == modifier.getValue().longValue()) {
                    rollIndexes.replace(rollIndex.getKey(),
                        ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                }
            }
            return Pair.of(true, rollIndexes);
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair.of(false, "Greater than resulting value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() > modifier.getSelectorValue()) {
                        rollIndexes.replace(rollIndex.getKey(),
                            ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                    }
                }
                return Pair.of(true, rollIndexes);
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() < modifier.getSelectorValue()) {
                        rollIndexes.replace(rollIndex.getKey(),
                            ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                    }
                }
                return Pair.of(true, rollIndexes);
            case LOW:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Lowest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Lowest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return highLowRerollOnceIteration(rollIndexes, modifier, die);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return highLowRerollOnceIteration(rollIndexes, modifier, die);
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> processRerollAdd(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == modifier.getValue().longValue()) {
                    rollIndexes.put((long) rollIndexes.size(),
                        ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                    break;
                }
            }
            return Pair.of(true, rollIndexes);
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair.of(false, "Greater than resulting value cannot be equal to or higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() > modifier.getSelectorValue()) {
                        rollIndexes.put((long) rollIndexes.size(),
                            ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                        break;
                    }
                }
                return Pair.of(true, rollIndexes);
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() < modifier.getSelectorValue()) {
                        rollIndexes.put((long) rollIndexes.size(),
                            ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                        break;
                    }
                }
                return Pair.of(true, rollIndexes);
            case LOW:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Lowest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Lowest selector value cannot be less than or equal to 0");
                }
                rollIndexes.put((long) rollIndexes.size(),
                    ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                return Pair.of(true, rollIndexes);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes.put((long) rollIndexes.size(),
                    ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
                return Pair.of(true, rollIndexes);
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> processMinimum(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() < modifier.getValue()) {
                    rollIndexes.replace(rollIndex.getKey(), modifier.getValue().longValue());
                }
            }
            return Pair.of(true, rollIndexes);
        } else {
            return Pair.of(false, "Selector `" + modifier.getSelector().getLiteral() + "` is not a valid selector for "
                + "the minimum modifier");
        }
    }

    private static Pair<Boolean, Object> processMaximum(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() > modifier.getValue()) {
                    rollIndexes.replace(rollIndex.getKey(), modifier.getValue().longValue());
                }
            }
            return Pair.of(true, rollIndexes);
        } else {
            return Pair.of(false, "Selector `" + modifier.getSelector().getLiteral() + "` is not a valid selector for "
                + "the maximum modifier");
        }
    }

    private static Pair<Boolean, Object> processExplode(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == modifier.getValue().longValue()) {
                    while (true) {
                        long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                        rollIndexes.put((long) rollIndexes.size(), res);
                        if (res != modifier.getValue()) {
                            break;
                        }
                    }
                }
            }
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair.of(false, "Greater than resulting value cannot be equal to or higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() > modifier.getSelectorValue().longValue()) {
                        while (true) {
                            long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                            rollIndexes.put((long) rollIndexes.size(), res);
                            if (res <= modifier.getSelectorValue()) {
                                break;
                            }
                        }
                    }
                }
                break;
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() < modifier.getSelectorValue().longValue()) {
                        while (true) {
                            long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                            rollIndexes.put((long) rollIndexes.size(), res);
                            if (res >= modifier.getSelectorValue()) {
                                break;
                            }
                        }
                    }
                }
                break;
            case LOW:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Lowest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Lowest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return highLowExplodeIteration(rollIndexes, modifier, die);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return highLowExplodeIteration(rollIndexes, modifier, die);
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> processKeep(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() != modifier.getValue().longValue()) {
                    rollIndexes.remove(rollIndex.getKey());
                }
            }
            return Pair.of(true, rollIndexes);
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair.of(false, "Greater than resulting value cannot be equal to or higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() <= modifier.getSelectorValue()) {
                        rollIndexes.remove(rollIndex.getKey());
                    }
                }
                return Pair.of(true, rollIndexes);
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() >= modifier.getSelectorValue()) {
                        rollIndexes.remove(rollIndex.getKey());
                    }
                }
                return Pair.of(true, rollIndexes);
            case LOW:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Lowest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Lowest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .limit(modifier.getSelectorValue().longValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .limit(modifier.getSelectorValue().longValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> processDrop(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == modifier.getValue().longValue()) {
                    rollIndexes.remove(rollIndex.getKey());
                }
            }
            return Pair.of(true, rollIndexes);
        }
        switch (modifier.getSelector()) {
            case GREATER_THAN:
                if (modifier.getSelectorValue() >= die.getDiceValue()) {
                    return Pair.of(false, "Greater than resulting value cannot be equal to or higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Greater than selector value cannot be less than or equal to 0");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() >= modifier.getSelectorValue()) {
                        rollIndexes.remove(rollIndex.getKey());
                    }
                }
                return Pair.of(true, rollIndexes);
            case LESS_THAN:
                if (modifier.getSelectorValue() > die.getDiceValue()) {
                    return Pair.of(false, "Less than selector value cannot be higher than maximum die value");
                } else if (modifier.getSelectorValue() <= 1) {
                    return Pair.of(false, "Less than selector value cannot be less than or equal to 1");
                }
                for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                    if (rollIndex.getValue() <= modifier.getSelectorValue()) {
                        rollIndexes.remove(rollIndex.getKey());
                    }
                }
                return Pair.of(true, rollIndexes);
            case LOW:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Lowest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Lowest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream().sorted(Map.Entry.comparingByValue())
                    .skip(modifier.getSelectorValue().longValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .skip(modifier.getSelectorValue().longValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> highLowRerollOnceIteration(Map<Long, Long> rollIndexes, Modifier modifier, Die die) {
        int iteration = 0;
        for (Map.Entry<Long, Long> entry : rollIndexes.entrySet()) {
            rollIndexes.replace(entry.getKey(),
                ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
            iteration++;
            if (iteration >= modifier.getSelectorValue()) {
                break;
            }
        }
        return Pair.of(true, rollIndexes);
    }

    private static Pair<Boolean, Object> highLowExplodeIteration(Map<Long, Long> rollIndexes, Modifier modifier, Die die) {
        int iteration = 0;
        for (Map.Entry<Long, Long> ignored : rollIndexes.entrySet()) {
            rollIndexes.put((long) rollIndexes.size(), ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
            iteration++;
            if (iteration >= modifier.getSelectorValue()) {
                break;
            }
        }
        return Pair.of(true, rollIndexes);
    }
}
