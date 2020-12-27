import com.mongodb.client.model.Filters;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;

public class ClassInitialisationSpeedTest {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        System.out.println("---------------------------------------------------------");
        System.out.println("Testing global initialisation times...");
        long subTime = System.currentTimeMillis();
        ElementRegistry.getInstance();
        System.out.println("Initialised in: " + (System.currentTimeMillis() - subTime) + "ms");
        System.out.println("---------------------------------------------------------");
        System.out.println("Performing 10000 load, edit, and re-save tests...");
        subTime = System.currentTimeMillis();
        for (int i = 0; i < 10000; i++) {
            Source source = ElementRegistry.getInstance().findSourcesByOwnerID("277385484919898112").get(0);
            if (i % 2 == 0) {
                source.addDiscordID("627911250936070187");
            } else {
                source.removeDiscordID("627911250936070187");
            }
            ElementRegistry.getInstance().getSources().findOneAndReplace(Filters.eq("_id", source.getUUID()), source);
        }
        subTime = System.currentTimeMillis() - subTime;
        System.out.println("10000 iterations complete. Time taken: " + subTime + "ms (" + (subTime / 1000) + " seconds)");
        System.out.println("---------------------------------------------------------");
        System.out.println("Total time taken: " + (System.currentTimeMillis() - time) + "ms");
    }
}
