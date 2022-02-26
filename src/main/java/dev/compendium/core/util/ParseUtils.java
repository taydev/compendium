package dev.compendium.core.util;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.spell.Spell;
import dev.compendium.core.util.entry.Entry;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class ParseUtils {

    public static List<Entry> parseEntries(JsonArray jsonArray) {
        List<Entry> entries = new ArrayList<>();
        for (JsonElement jsonEntry : jsonArray) {
            Entry entry = null;
            if (jsonEntry.isJsonObject()) {
                JsonObject entryObject = jsonEntry.getAsJsonObject();
                String type = entryObject.get("type").getAsString();
                switch (type) {
                    case "entries":
                        String name = entryObject.get("name").getAsString();
                        JsonArray innerEntries = entryObject.get("entries").getAsJsonArray();
                        List<Entry> innerEntriesList = parseEntries(innerEntries);
                        entry = new Entry(Entry.EntryType.ENTRIES, name, innerEntriesList);
                        break;
                    case "table":
                        String caption = entryObject.get("caption").getAsString();
                        JsonArray columnLabelArray = entryObject.get("colLabels").getAsJsonArray();
                        StringBuilder columnLabelBuilder = new StringBuilder();
                        for (JsonElement columnLabel : columnLabelArray) {
                            columnLabelBuilder.append(columnLabel.getAsString());
                            if (columnLabelArray.get(columnLabelArray.size() - 1) != columnLabel) {
                                columnLabelBuilder.append("|");
                            }
                        }
                        String columnLabels = columnLabelBuilder.toString().trim();
                        JsonArray columnRowArray = entryObject.get("rows").getAsJsonArray();
                        List<String> columnRows = new ArrayList<>();
                        for (JsonElement columnRow : columnRowArray) {
                            JsonArray columnRowElementArray = columnRow.getAsJsonArray();
                            StringBuilder columnRowBuilder = new StringBuilder();
                            for (JsonElement columnRowElement : columnRowElementArray) {
                                columnRowBuilder.append(columnRowElement.getAsString());
                                if (columnRowElementArray.get(columnRowElementArray.size() - 1) != columnRowElement) {
                                    columnRowBuilder.append("|");
                                }
                            }
                            columnRows.add(columnRowBuilder.toString().trim());
                        }
                        entry = new Entry(caption, columnLabels, columnRows);
                        break;
                    case "list":
                        List<String> items = new ArrayList<>();
                        JsonArray itemArray = entryObject.get("items").getAsJsonArray();
                        for (JsonElement itemElement : itemArray) {
                            items.add(itemElement.getAsString());
                        }
                        entry = new Entry(items);
                        break;
                    default:
                        ElementRegistry.getLogger().warn("Unusual behaviour in entries parsing: found type {}?", type);
                }
            } else {
                entry = new Entry(jsonEntry.getAsString());
            }
            if (entry != null) {
                entries.add(entry);
            }
        }
        return entries;
    }

}
