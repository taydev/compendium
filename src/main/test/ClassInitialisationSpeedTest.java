import dev.compendium.core.ElementRegistry;


public class ClassInitialisationSpeedTest {

    public static void main(String[] args) {
        long time = System.currentTimeMillis();
        System.out.println("---------------------------------------------------------");
        System.out.println("Testing global initialisation times...");
        long subTime = System.currentTimeMillis();
        ElementRegistry.getInstance();
        System.out.println("Initialised in: " + (System.currentTimeMillis() - subTime) + "ms");
        System.out.println("---------------------------------------------------------");

        System.out.println("---------------------------------------------------------");
        System.out.println("Total time taken: " + (System.currentTimeMillis() - time) + "ms");
    }
}
