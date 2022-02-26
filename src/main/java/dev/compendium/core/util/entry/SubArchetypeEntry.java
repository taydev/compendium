package dev.compendium.core.util.entry;

public class SubArchetypeEntry extends ArchetypeEntry {

    private final String subArchetypeName;
    private final String subArchetypeSource;
    private String subSubArchetypeName;

    public SubArchetypeEntry(String archetypeName, String source, String subArchetypeName, String subArchetypeSource) {
        this(archetypeName, source, subArchetypeName, subArchetypeSource, "");
    }


    public SubArchetypeEntry(String archetypeName, String source, String subArchetypeName, String subArchetypeSource, String subSubArchetypeName) {
        super(archetypeName, source);
        this.subArchetypeName = subArchetypeName;
        this.subArchetypeSource = subArchetypeSource;
        this.subSubArchetypeName = subSubArchetypeName;
    }

    public String getSubArchetypeName() {
        return this.subArchetypeName;
    }

    public String getSubArchetypeSource() {
        return this.subArchetypeSource;
    }

    public String getSubSubArchetypeName() {
        return subSubArchetypeName;
    }

    public void setSubSubArchetypeName(String subSubArchetypeName) {
        this.subSubArchetypeName = subSubArchetypeName;
    }
}
