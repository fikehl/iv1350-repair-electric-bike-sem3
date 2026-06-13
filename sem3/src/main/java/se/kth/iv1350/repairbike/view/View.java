package se.kth.iv1350.repairbike.view;

import java.time.format.DateTimeFormatter;
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
 * the controller is printed to {@code System.out}. Since presentation is
 * the responsibility of the view, this class formats the returned data-only
 * DTOs itself before displaying them.
 */
public class View {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
        System.out.println(formatRepairOrder(afterProblem));

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
        System.out.println(formatRepairOrder(afterDiagnostic));

        printSeparator();
        System.out.println("RECEPTIONIST: Customer accepts the proposed repair tasks."
                           + " Registering acceptance.");
        RepairOrderDTO afterAcceptance = controller.acceptRepairOrder();
        System.out.println("System returned the accepted repair order:");
        System.out.println(formatRepairOrder(afterAcceptance));
        printSeparator();
    }

    private String formatRepairOrder(RepairOrderDTO order) {
        StringBuilder builder = new StringBuilder();
        builder.append("Repair order id:      ").append(order.getRepairOrderId()).append("\n");
        builder.append("State:                ").append(order.getState()).append("\n");
        builder.append("Created:              ")
               .append(order.getCreationTime().format(DATE_TIME_FORMATTER)).append("\n");
        builder.append("Customer:             ").append(order.getCustomer().getName())
               .append(", ").append(order.getCustomer().getPhoneNumber()).append("\n");
        builder.append("Bike:                 ")
               .append(order.getCustomer().getBike().getBrand()).append(" ")
               .append(order.getCustomer().getBike().getModel()).append("\n");
        builder.append("Problem description:  ").append(order.getProblemDescription()).append("\n");
        if (order.getDiagnosticDescription() == null) {
            builder.append("Diagnostic report:    (none yet)\n");
        } else {
            builder.append("Diagnostic report:    ")
                   .append(order.getDiagnosticDescription()).append("\n");
            builder.append("Proposed repair tasks:\n");
            for (RepairTask task : order.getProposedTasks()) {
                builder.append("  - ").append(task).append("\n");
            }
            builder.append("Total cost:           ").append(order.getTotalCost()).append("\n");
        }
        builder.append("Estimated completion: ")
               .append(order.getEstimatedCompletionTime().format(DATE_TIME_FORMATTER));
        return builder.toString();
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
