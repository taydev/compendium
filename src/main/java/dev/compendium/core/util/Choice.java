package dev.compendium.core.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.bson.Document;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Choice {

    @BsonProperty("choice_count")
    private final int choiceCount;
    @BsonProperty("choices")
    private final List<String> choices;
    @BsonProperty("type")
    private final String type;

    public Choice(@BsonProperty("type") String type, @BsonProperty("choice_count") int choiceCount,
        @BsonProperty("choices") String... choices) {
        this.type = type;
        this.choiceCount = choiceCount;
        if (!choices[0].equals("")) {
            this.choices = new ArrayList<>(Arrays.asList(choices));
        } else {
            this.choices = new ArrayList<>();
        }
    }

    @BsonIgnore
    public static Choice parse(Document document) {
        Choice choice = new Choice(document.getString("type"), document.getInteger("choice_count"), "");
        List<String> choices = (List<String>) document.get("choices");
        choice.getChoices().addAll(choices);
        return choice;
    }

    @BsonProperty("type")
    public String getType() {
        return this.type;
    }

    @BsonProperty("choice_count")
    public int getChoiceCount() {
        return this.choiceCount;
    }

    @BsonProperty("choices")
    public List<String> getChoices() {
        return this.choices;
    }

    //test
    @BsonIgnore
    public Document convertToDocument() {
        Metadata metadata = new Metadata();
        metadata.set("type", this.getType())
            .set("choice_count", this.getChoiceCount())
            .set("choices", this.getChoices());
        return metadata.getDocument();
    }
}
