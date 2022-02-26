package dev.compendium.core.util.entry;

public class ArchetypeEntry {
    
    private final String archetypeName;
    private final String source;
    
    public ArchetypeEntry(String archetypeName, String source) {
        this.archetypeName = archetypeName;
        this.source = source;
    }

    public String getArchetypeName() {
        return this.archetypeName;
    }

    public String getSource() {
        return this.source;
    }
}
