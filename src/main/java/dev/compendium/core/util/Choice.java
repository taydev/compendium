package dev.compendium.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Choice {

    private final int choiceCount;
    private final List<Object> choices;

    public Choice(int choiceCount, Object... choices) {
        this.choiceCount = choiceCount;
        this.choices = new ArrayList<>(Arrays.asList(choices));
    }

    public int getChoiceCount() {
        return this.choiceCount;
    }

    public List<Object> getChoices() {
        return this.choices;
    }

}
