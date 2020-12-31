import com.mongodb.client.model.Filters;
import dev.compendium.core.ElementRegistry;
import dev.compendium.core.util.Source;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class CompendiumTestingFacility {

    private static final Logger LOGGER = LoggerFactory.getLogger("CTF");

    public static void main(String[] args) {
        long totalTime = System.currentTimeMillis();
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Initialising element registry...");
        long time = System.currentTimeMillis();
        ElementRegistry.getInstance();
        LOGGER.info("Element registry initialised. Time taken: {}ms", System.currentTimeMillis() - time);
        LOGGER.info("----------------------------------------------------------------");
        LOGGER.info("Creating testing source...");
        time = System.currentTimeMillis();
        Source source = new Source("Compendium Testing Facility", "ults", "277385484919898112");
        LOGGER.info("Testing source created. Storing...");
        ElementRegistry.getInstance().storeSource(source);
        LOGGER.info("----------------------------------------------------------------");
        System.out.println("Total time taken: " + (System.currentTimeMillis() - totalTime) + "ms");
    }
}
