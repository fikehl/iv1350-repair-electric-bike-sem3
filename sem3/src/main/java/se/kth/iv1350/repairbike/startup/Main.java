package se.kth.iv1350.repairbike.startup;

import se.kth.iv1350.repairbike.controller.Controller;
import se.kth.iv1350.repairbike.integration.Printer;
import se.kth.iv1350.repairbike.integration.RegistryCreator;
import se.kth.iv1350.repairbike.view.View;

/**
 * Contains the {@code main} method. Performs all startup of the application.
 */
public class Main {

    /**
     * The application's entry point. No command line arguments are used.
     *
     * @param args The application does not take any command line parameters.
     */
    public static void main(String[] args) {
        RegistryCreator registryCreator = new RegistryCreator();
        Printer printer = new Printer();
        Controller controller = new Controller(registryCreator, printer);
        View view = new View(controller);
        view.sampleExecution();
    }
}
