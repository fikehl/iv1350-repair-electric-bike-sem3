package se.kth.iv1350.repairbike.view;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairbike.controller.Controller;
import se.kth.iv1350.repairbike.integration.CustomerDTO;
import se.kth.iv1350.repairbike.model.Amount;
import se.kth.iv1350.repairbike.model.RepairOrderDTO;
import se.kth.iv1350.repairbike.model.RepairTask;

/**
 * This program has no real view, this class instead simulates user input by
 * making hard-coded calls to the controller. Everything that is returned by
 * the controller is printed to {@code System.out}.
 */
public class View {
    private final Controller controller;

    /**
     * Creates a new instance.
     *
     * @param controller The controller used for all calls to the model.
     */
    public View(Controller controller) {
        this.controller = controller;
    }

    /**
     * Performs a sample execution that walks through the basic flow of the
     * Repair Electric Bike use case from start to end.
     */
    public void sampleExecution() {
        printSeparator();
        System.out.println("RECEPTIONIST: Customer arrives. Searching for customer with"
                           + " phone 0701112233.");
        CustomerDTO customer = controller.searchCustomer("0701112233");
        System.out.println("System returned: " + customer);

        printSeparator();
        System.out.println("RECEPTIONIST: Customer describes the problem. Registering it.");
        String problemDescription =
                "The motor cuts out after about ten minutes of use, and the front brake"
                + " squeaks loudly when applied.";
        RepairOrderDTO afterProblem = controller.registerProblem(customer, problemDescription);
        System.out.println("System returned a new repair order:");
        System.out.println(afterProblem.getPrintout());

        printSeparator();
        System.out.println("TECHNICIAN: Performing diagnostic and proposing repair tasks.");
        String diagnosticDescription =
                "The battery management system has a faulty temperature sensor that"
                + " triggers an emergency shutdown. The front brake pads are worn"
                + " down to the wear indicators.";
        List<RepairTask> proposedTasks = buildProposedTasks();
        RepairOrderDTO afterDiagnostic =
                controller.registerDiagnostic(diagnosticDescription, proposedTasks);
        System.out.println("System returned the updated repair order:");
        System.out.println(afterDiagnostic.getPrintout());

        printSeparator();
        System.out.println("RECEPTIONIST: Customer accepts the proposed repair tasks."
                           + " Registering acceptance.");
        RepairOrderDTO afterAcceptance = controller.acceptRepairOrder();
        System.out.println("System returned the accepted repair order:");
        System.out.println(afterAcceptance.getPrintout());
        printSeparator();
    }

    private List<RepairTask> buildProposedTasks() {
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace battery temperature sensor", new Amount(1200)));
        tasks.add(new RepairTask("Replace front brake pads", new Amount(450)));
        tasks.add(new RepairTask("Full safety inspection", new Amount(350)));
        return tasks;
    }

    private void printSeparator() {
        System.out.println();
        System.out.println("############################################"
                           + "############################");
        System.out.println();
    }
}
