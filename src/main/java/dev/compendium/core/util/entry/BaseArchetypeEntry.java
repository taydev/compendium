package dev.compendium.core.util.entry;

public class BaseArchetypeEntry extends ArchetypeEntry {

    private final String baseArchetypeName;
    private final String baseArchetypeSource;

    public BaseArchetypeEntry(String archetypeName, String source, String baseArchetypeName, String baseArchetypeSource) {
        super(archetypeName, source);
        this.baseArchetypeName = baseArchetypeName;
        this.baseArchetypeSource = baseArchetypeSource;
    }

    public String getBaseArchetypeName() {
        return this.baseArchetypeName;
    }

    public String getBaseArchetypeSource() {
        return this.baseArchetypeSource;
    }
}
