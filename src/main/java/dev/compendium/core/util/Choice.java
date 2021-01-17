package dev.compendium.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;

public class Choice {

    private final int choiceCount;
    private final List<Object> choices;
    private final String type;

    public Choice(String type, int choiceCount, Object... choices) {
        this.type = type;
        this.choiceCount = choiceCount;
        this.choices = new ArrayList<>(Arrays.asList(choices));
    }

    public String getType() {
        return this.type;
    }

    public int getChoiceCount() {
        return this.choiceCount;
    }

    public List<Object> getChoices() {
        return this.choices;
    }

    public Document convertToDocument() {
        Metadata metadata = new Metadata();
        metadata.set("type", this.getType())
            .set("choice_count", this.getChoiceCount())
            .set("choices", this.getChoices());
        return metadata.getDocument();
    }
}
