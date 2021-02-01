package dev.compendium.core.util;

import com.google.gson.JsonObject;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.spell.Spell;
import java.util.Arrays;
import java.util.List;

public class ParseUtils {

    public static void parseSpell(JsonObject jsonObject) {
        Source source = ElementRegistry.getInstance().findSourcesByOwnerID("-1").get(0);
        Spell spell = new Spell(source, jsonObject.get("name").getAsString());
        spell.setDescription(jsonObject.get("desc").getAsString());
        if (jsonObject.has("higher_level")) {
            spell.setUpcastDescription(jsonObject.get("higher_level").getAsString());
        }
        String[] splitRange = jsonObject.get("range").getAsString().split(" ");
        try {
            int range = Integer.parseInt(splitRange[0]);
            RangeUnit unit = RangeUnit.getRange(splitRange[1]);
            spell.setCastingRange(range);
            spell.setCastingRangeUnit(unit);
        } catch (NumberFormatException ignored) {
            spell.setCastingRange(0);
            spell.setCastingRangeUnit(RangeUnit.TOUCH);
        }
        List<String> components = Arrays.asList(jsonObject.get("components").getAsString().split(", "));
        if (components.contains("V")) {
            spell.setVerbalComponent(true);
        }
        if (components.contains("S")) {
            spell.setSomaticComponent(true);
        }
        if (components.contains("M")) {
            spell.setMaterialComponent(true);
            spell.setMaterials(jsonObject.get("material").getAsString());
        }
        if (jsonObject.get("ritual").getAsString().equals("yes")) {
            spell.setRitual(true);
        }
        String[] splitDuration = jsonObject.get("duration").getAsString().split(" ");
        try {
            int duration = Integer.parseInt(splitDuration[0]);
            TimeUnit unit = TimeUnit.getTime(splitDuration[1]);
            spell.setDuration(duration);
            spell.setDurationUnit(unit);
        } catch (NumberFormatException ignored) {
            spell.setDuration(0);
            spell.setDurationUnit(TimeUnit.INSTANTANEOUS);
        }
        String castTimeString = jsonObject.get("casting_time").getAsString();
        String[] splitCastTime = castTimeString.split(" ");
        try {
            int castTime = Integer.parseInt(splitCastTime[0]);
            TimeUnit unit = TimeUnit.getTime(splitCastTime[1]);
            spell.setCastingDuration(castTime);
            spell.setCastingTimeUnit(unit);
            if (unit == TimeUnit.REACTION && castTimeString.contains(",")) {
                spell.setReactionCondition(castTimeString.substring(castTimeString.indexOf(",") + 1).trim());
            }
        } catch (NumberFormatException ignored) {
            spell.setCastingDuration(1);
            spell.setCastingTimeUnit(TimeUnit.ACTION);
        }
        spell.setLevel(jsonObject.get("level_int").getAsInt());
        spell.setSchool(ElementRegistry.getInstance().findMagicSchoolsByName(jsonObject.get("school").getAsString()).get(0));
        ElementRegistry.getInstance().storeSpell(spell);
    }
}
