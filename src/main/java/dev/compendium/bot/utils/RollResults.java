package dev.compendium.bot.utils;

import java.util.Map;
import uk.co.binaryoverload.dicerollparser.objects.DiceRoll;

public class RollResults {

    private Map<DiceRoll, Long> rollResults;
    private Map<String, Long> labelledRolls;
    private long totalRoll;
    private double trueResult;
    private StringBuilder rollString;

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
