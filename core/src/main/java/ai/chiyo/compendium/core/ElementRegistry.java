package ai.chiyo.compendium.core;

public class ElementRegistry {

    private static ElementRegistry instance;

    public ElementRegistry() {

    }

    public static ElementRegistry getInstance() {
        if (instance == null) {
            instance = new ElementRegistry();
        }

        return instance;
    }

}
