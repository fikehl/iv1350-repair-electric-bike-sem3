package se.kth.iv1350.repairbike.controller;

import java.util.List;
import se.kth.iv1350.repairbike.integration.CustomerDTO;
import se.kth.iv1350.repairbike.integration.CustomerRegistry;
import se.kth.iv1350.repairbike.integration.Printer;
import se.kth.iv1350.repairbike.integration.RegistryCreator;
import se.kth.iv1350.repairbike.integration.RepairOrderRegistry;
import se.kth.iv1350.repairbike.model.DiagnosticReport;
import se.kth.iv1350.repairbike.model.RepairOrder;
import se.kth.iv1350.repairbike.model.RepairOrderDTO;
import se.kth.iv1350.repairbike.model.RepairTask;

/**
 * The application's only controller class. All calls from the view to the
 * model and to the integration layer pass through this class. The controller
 * holds the current repair order while it is being built up (created,
 * diagnosed, accepted) and returns immutable {@link RepairOrderDTO}
 * snapshots to the view so the entity stays intact.
 */
public class Controller {
    private final CustomerRegistry customerRegistry;
    private final RepairOrderRegistry repairOrderRegistry;
    private final Printer printer;

    private RepairOrder currentRepairOrder;

    /**
     * Creates a new instance.
     *
     * @param registryCreator Used to obtain references to all registries.
     * @param printer         The printer used when a repair order is printed.
     */
    public Controller(RegistryCreator registryCreator, Printer printer) {
        this.customerRegistry = registryCreator.getCustomerRegistry();
        this.repairOrderRegistry = registryCreator.getRepairOrderRegistry();
        this.printer = printer;
    }

    /**
     * Searches for the customer with the specified phone number.
     *
     * @param phoneNumber The phone number identifying the customer.
     * @return The customer matching the specified phone number, or
     *         {@code null} if no such customer was found.
     */
    public CustomerDTO searchCustomer(String phoneNumber) {
        return customerRegistry.findCustomer(phoneNumber);
    }

    /**
     * Creates a new repair order for the specified customer with the
     * specified problem description, and stores the order in the repair
     * order registry. The new order becomes the current repair order.
     *
     * @param customer           The customer that handed in the bike.
     * @param problemDescription The problem description given by the customer.
     * @return An immutable snapshot of the newly created repair order.
     */
    public RepairOrderDTO registerProblem(CustomerDTO customer, String problemDescription) {
        int id = repairOrderRegistry.nextRepairOrderId();
        currentRepairOrder = new RepairOrder(id, customer, problemDescription);
        repairOrderRegistry.addRepairOrder(currentRepairOrder);
        return currentRepairOrder.toDTO();
    }

    /**
     * Adds the technician's diagnostic findings and the proposed repair tasks
     * to the current repair order, and updates the order in the registry.
     *
     * @param diagnosticDescription The technician's diagnostic findings.
     * @param proposedTasks         The repair tasks proposed by the technician.
     * @return An immutable snapshot of the updated repair order.
     */
    public RepairOrderDTO registerDiagnostic(String diagnosticDescription,
                                             List<RepairTask> proposedTasks) {
        DiagnosticReport report = new DiagnosticReport(diagnosticDescription, proposedTasks);
        currentRepairOrder.addDiagnosticReport(report);
        repairOrderRegistry.updateRepairOrder(currentRepairOrder);
        return currentRepairOrder.toDTO();
    }

    /**
     * Marks the current repair order as accepted by the customer. The order
     * itself prints itself on the printer; the controller is not involved
     * in producing or sending the printout.
     *
     * @return An immutable snapshot of the accepted repair order.
     */
    public RepairOrderDTO acceptRepairOrder() {
        currentRepairOrder.accept(printer);
        repairOrderRegistry.updateRepairOrder(currentRepairOrder);
        return currentRepairOrder.toDTO();
    }
}
