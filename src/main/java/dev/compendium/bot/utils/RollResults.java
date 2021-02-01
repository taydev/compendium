package dev.compendium.bot.utils;

import java.util.Map;
import uk.co.binaryoverload.dicerollparser.objects.DiceRoll;

public class RollResults {

    private final Map<DiceRoll, Long> rollResults;
    private final Map<String, Long> labelledRolls;
    private final long totalRoll;
    private final double trueResult;
    private final StringBuilder rollString;

    public RollResults(Map<DiceRoll, Long> rollResults, Map<String, Long> labelledRolls, long totalRoll, double trueResult,
        StringBuilder rollString) {
        this.rollResults = rollResults;
        this.labelledRolls = labelledRolls;
        this.totalRoll = totalRoll;
        this.trueResult = trueResult;
        this.rollString = rollString;
    }

    public Map<DiceRoll, Long> getRollResults() {
        return this.rollResults;
    }

    public Map<String, Long> getLabelledRolls() {
        return this.labelledRolls;
    }

    public long getTotalRoll() {
        return this.totalRoll;
    }

    public double getTrueResult() {
        return this.trueResult;
    }

    public StringBuilder getRollString() {
        return this.rollString;
    }
}
