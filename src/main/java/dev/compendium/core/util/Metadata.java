package dev.compendium.core.util;

import org.bson.Document;

public class Metadata {

    private Document document;

    public Metadata() {
        this(new Document());
    }

    public Metadata(Document document) {
        this.document = document;
    }

    public Document getDocument() {
        return this.document;
    }

    public void setDocument(Document document) {
        this.document = document;
    }

    public void set(String key, Object value) {
        this.setDocument(this.getDocument().append(key, value));
    }
}
