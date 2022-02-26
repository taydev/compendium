package dev.compendium.core.util.entry;

import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

import org.bson.codecs.pojo.annotations.BsonCreator;
import org.bson.codecs.pojo.annotations.BsonIgnore;
import org.bson.codecs.pojo.annotations.BsonProperty;

public class Entry {

    private final EntryType type;
    private String name;
    private final List<Entry> entries;

    private String content;

    private String caption;
    private String columnLabels;
    private List<String> rowContents;

    private List<String> items;

    public Entry(EntryType type, String name) {
        this(type, name, new ArrayList<>());
    }

    public Entry(EntryType type, String name, List<Entry> entries) {
        this.type = type;
        this.name = name;
        this.entries = entries;
    }

    public Entry(String text) {
        this(EntryType.ENTRIES, "");
        this.content = text;
    }

    public Entry(String caption, String columnLabels, List<String> rowContents) {
        this(EntryType.TABLE, caption);
        this.caption = caption;
        this.columnLabels = columnLabels;
        this.rowContents = rowContents;
    }

    public Entry(List<String> items) {
        this(EntryType.LIST, "");
        this.items = items;
    }

    public EntryType getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Entry> getEntries() {
        return this.entries;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCaption() {
        return this.caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getColumnLabels() {
        return this.columnLabels;
    }

    public void setColumnLabels(String columnLabels) {
        this.columnLabels = columnLabels;
    }

    public List<String> getRowContents() {
        return this.rowContents;
    }

    public List<String> getItems() {
        return this.items;
    }

    public enum EntryType {
        ENTRIES,
        TABLE,
        LIST;
    }
}
