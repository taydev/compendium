package dev.compendium.bot.commands.dnd;

import dev.compendium.bot.commands.CommandCategory;
import dev.compendium.bot.commands.ICommand;
import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
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
import uk.co.binaryoverload.dicerollparser.objects.DiceRoll;
import uk.co.binaryoverload.dicerollparser.objects.Die;
import uk.co.binaryoverload.dicerollparser.objects.Modifier;

// have to suppress dupe code because intellij doesn't like my min/max methods
@SuppressWarnings("DuplicatedCode")
public class RollCommand implements ICommand {

    @Override
    public String getCommand() {
        return "roll";
    }

    @Override
    public String[] getAliases() {
        return new String[]{"r", "dice", "die", "d"};
    }

    @Override
    public String getDescription() {
        return "Roll the dice!";
    }

    @Override
    public CommandCategory getCategory() {
        return CommandCategory.DND;
    }

    // everything from here until line 125 is a fucking mess
    @Override
    public void onCommand(User sender, Message message, MessageChannel channel, String command, String[] args) {
        if (args.length > 0) {
            List<DiceRoll> rolls = Parser.parseDiceRoll(args[0]);
            StringBuilder sb = new StringBuilder("**Roll:** ");
            Map<DiceRoll, Map<Die, List<Long>>> finalRollValues = new HashMap<>();
            for (DiceRoll roll : rolls) {
                System.out.println(roll.toString());
                Map<Die, List<Long>> diceRollValues = new HashMap<>();
                for (Die die : roll.getDice()) {
                    List<Long> rollValues = new ArrayList<>();
                    if (die.getDiceCount() <= 0) {
                        this.sendErrorMessage(channel, "Invalid roll: Cannot have 0 dice "
                            + "(`" + die.getDiceCount() + "d" + die.getDiceValue() + "`)");
                        break;
                    }
                    for (int i = 0; i < die.getDiceCount(); i++) {
                        long rollValue = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                        rollValues.add(rollValue);
                    }
                    diceRollValues.put(die, rollValues);
                }
                Pair<Boolean, Object> modifierResult = this.processModifiers(diceRollValues, roll);
                if (modifierResult.getLeft()) {
                    diceRollValues = (Map<Die, List<Long>>) modifierResult.getRight();
                } else {
                    this.sendErrorMessage(channel, "Roll failed while applying modifiers: "
                        + modifierResult.getRight());
                    return;
                }
                finalRollValues.put(roll, diceRollValues);
            }
            for (Map.Entry<DiceRoll, Map<Die, List<Long>>> entry : finalRollValues.entrySet()) {
                for (Map.Entry<Die, List<Long>> innerEntry : entry.getValue().entrySet()) {
                    channel.sendMessage(Arrays.toString(innerEntry.getValue().toArray())).queue();
                }
            }
            /*
                if (dieRollValues.size() > 0) {
                    StringBuilder sb = new StringBuilder("**Result: **");
                    for (Map.Entry<Die, List<Long>> entry : dieRollValues.entrySet()) {
                        Die die = entry.getKey();
                        List<Long> rolledValues = entry.getValue();
                        long totalValue = 0;
                        long indicator = 1;
                        sb.append(die.getDiceCount()).append("d").append(die.getDiceValue()).append(" (");
                        for (long value : rolledValues) {
                            totalValue += value;
                            sb.append(value);
                            if (indicator != rolledValues.size()) {
                                sb.append(", ");
                            }
                            indicator++;
                        }
                        sb.append(")\n**Total: **").append(totalValue).append("\n");
                    }
                    channel.sendMessage(sb.toString().trim()).queue();
                }
                */
        } else {
            channel.sendMessage("invalid").queue();
        }
    }

    private void sendErrorMessage(MessageChannel channel, String error) {
        channel.sendMessage(new EmbedBuilder()
            .setColor(Color.decode("#990011"))
            .setTitle("Roll - Error")
            .setDescription(error)
            .build())
            .queue();
    }

    private Pair<Boolean, Object> processModifiers(Map<Die, List<Long>> diceRollValues, DiceRoll roll) {
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
                        result = this.processReroll(die, rollIndexes, modifier);
                        break;
                    case REROLL_ONE:
                        result = this.processRerollOnce(die, rollIndexes, modifier);
                        break;
                    case REROLL_ADD:
                        result = this.processRerollAdd(die, rollIndexes, modifier);
                        break;
                    case MINIMUM:
                        result = this.processMinimum(die, rollIndexes, modifier);
                        break;
                    case MAXIMUM:
                        result = this.processMaximum(die, rollIndexes, modifier);
                        break;
                    case EXPLODE:
                        result = this.processExplode(die, rollIndexes, modifier);
                        break;
                    case KEEP:
                        result = this.processKeep(die, rollIndexes, modifier);
                        break;
                    case DROP:
                        result = this.processDrop(die, rollIndexes, modifier);
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

    private Pair<Boolean, Object> processReroll(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            do {
                rollIndexes.replaceAll((k, v) -> v = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
            } while (rollIndexes.containsValue((long) modifier.getValue()));
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
                            ThreadLocalRandom.current().nextLong(1, modifier.getSelectorValue() + 1));
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
                            ThreadLocalRandom.current().nextLong(modifier.getSelectorValue(), die.getDiceValue() + 1));
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

    private Pair<Boolean, Object> processRerollOnce(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == (long) modifier.getValue()) {
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
                return this.highLowRerollOnceIteration(rollIndexes, modifier, die);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return this.highLowRerollOnceIteration(rollIndexes, modifier, die);
        }
        return Pair.of(true, rollIndexes);
    }

    private Pair<Boolean, Object> processRerollAdd(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == (long) modifier.getValue()) {
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

    private Pair<Boolean, Object> processMinimum(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() < modifier.getValue()) {
                    rollIndexes.replace(rollIndex.getKey(), (long) modifier.getValue());
                }
            }
            return Pair.of(true, rollIndexes);
        } else {
            return Pair.of(false, "Selector `" + modifier.getSelector().getLiteral() + "` is not a valid selector for "
                + "the minimum modifier");
        }
    }

    private Pair<Boolean, Object> processMaximum(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() > modifier.getValue()) {
                    rollIndexes.replace(rollIndex.getKey(), (long) modifier.getValue());
                }
            }
            return Pair.of(true, rollIndexes);
        } else {
            return Pair.of(false, "Selector `" + modifier.getSelector().getLiteral() + "` is not a valid selector for "
                + "the maximum modifier");
        }
    }

    private Pair<Boolean, Object> processExplode(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == (long) modifier.getValue()) {
                    while (true) {
                        long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                        rollIndexes.put((long) rollIndexes.size(), res);
                        if (res != modifier.getValue())
                            break;
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
                    if (rollIndex.getValue() > (long) modifier.getSelectorValue()) {
                        while (true) {
                            long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                            rollIndexes.put((long) rollIndexes.size(), res);
                            if (res <= modifier.getSelectorValue())
                                break;
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
                    if (rollIndex.getValue() < (long) modifier.getSelectorValue()) {
                        while (true) {
                            long res = ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1);
                            rollIndexes.put((long) rollIndexes.size(), res);
                            if (res >= modifier.getSelectorValue())
                                break;
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
                return this.highLowExplodeIteration(rollIndexes, modifier, die);
            case HIGH:
                if (modifier.getSelectorValue() > rollIndexes.size()) {
                    return Pair.of(false, "Highest selector value cannot be higher than max present rolls");
                } else if (modifier.getSelectorValue() <= 0) {
                    return Pair.of(false, "Highest selector value cannot be less than or equal to 0");
                }
                rollIndexes = rollIndexes.entrySet().stream()
                    .sorted(Collections.reverseOrder(Map.Entry.comparingByValue()))
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return this.highLowExplodeIteration(rollIndexes, modifier, die);
        }
        return Pair.of(true, rollIndexes);
    }

    private Pair<Boolean, Object> processKeep(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() != (long) modifier.getValue()) {
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
                    .limit(modifier.getSelectorValue())
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
                    .limit(modifier.getSelectorValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
        }
        return Pair.of(true, rollIndexes);
    }

    private Pair<Boolean, Object> processDrop(Die die, Map<Long, Long> rollIndexes, Modifier modifier) {
        if (modifier.getSelector() == null) {
            if (modifier.getValue() > die.getDiceValue()) {
                return Pair.of(false, "Default selector value cannot be higher than maximum die value");
            } else if (modifier.getValue() <= 0) {
                return Pair.of(false, "Default selector value cannot be less than or equal to 0");
            }
            for (Map.Entry<Long, Long> rollIndex : rollIndexes.entrySet()) {
                if (rollIndex.getValue() == (long) modifier.getValue()) {
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
                    .skip(modifier.getSelectorValue())
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
                    .skip(modifier.getSelectorValue())
                    .collect(Collectors.toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
                return Pair.of(true, rollIndexes);
        }
        return Pair.of(true, rollIndexes);
    }

    private Pair<Boolean, Object> highLowRerollOnceIteration(Map<Long, Long> rollIndexes, Modifier modifier, Die die) {
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

    private Pair<Boolean, Object> highLowExplodeIteration(Map<Long, Long> rollIndexes, Modifier modifier, Die die) {
        int iteration = 0;
        for (Map.Entry<Long, Long> entry : rollIndexes.entrySet()) {
            rollIndexes.put((long) rollIndexes.size(), ThreadLocalRandom.current().nextLong(1, die.getDiceValue() + 1));
            iteration++;
            if (iteration >= modifier.getSelectorValue()) {
                break;
            }
        }
        return Pair.of(true, rollIndexes);
    }
}
