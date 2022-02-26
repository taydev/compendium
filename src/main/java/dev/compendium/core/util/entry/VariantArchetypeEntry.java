package dev.compendium.core.util.entry;

public class VariantArchetypeEntry extends ArchetypeEntry {

    private final String variantSource;

    public VariantArchetypeEntry(String archetypeName, String source, String variantSource) {
        super(archetypeName, source);
        this.variantSource = variantSource;
    }

    public String getVariantSource() {
        return this.variantSource;
    }
}
