package ai.chiyo.compendium.core.util;

public class DatabaseHandler {

    private static DatabaseHandler instance;

    public DatabaseHandler() {

    }

    public static DatabaseHandler getInstance() {
        if (instance == null) {
            instance = new DatabaseHandler();
        }

        return instance;
    }

}
