package se.kth.iv1350.repairbike.model;

import java.time.LocalDateTime;
import java.util.Collections;
import se.kth.iv1350.repairbike.integration.CustomerDTO;
import se.kth.iv1350.repairbike.integration.Printer;

/**
 * Represents one specific repair order, created when a customer hands in a
 * bike for repair. The order keeps track of the customer, the bike, the
 * customer's problem description, the technician's diagnostic report and
 * the current state of the order.
 */
public class RepairOrder {
    private static final int DEFAULT_REPAIR_DAYS = 7;

    private final int repairOrderId;
    private final CustomerDTO customer;
    private final String problemDescription;
    private final LocalDateTime creationTime;
    private DiagnosticReport diagnosticReport;
    private RepairOrderState state;

    /**
     * Creates a newly registered repair order. The state of the new order is
     * {@link RepairOrderState#NEWLY_CREATED}.
     *
     * @param repairOrderId      A unique id, used to identify this repair order.
     * @param customer           The customer that handed in the bike.
     * @param problemDescription The problem description given by the customer.
     */
    public RepairOrder(int repairOrderId, CustomerDTO customer, String problemDescription) {
        this.repairOrderId = repairOrderId;
        this.customer = customer;
        this.problemDescription = problemDescription;
        this.creationTime = LocalDateTime.now();
        this.state = RepairOrderState.NEWLY_CREATED;
    }

    /**
     * @return The unique id of this repair order.
     */
    public int getRepairOrderId() {
        return repairOrderId;
    }

    /**
     * @return The current state of this repair order.
     */
    public RepairOrderState getState() {
        return state;
    }

    /**
     * Adds the technician's diagnostic report and the proposed repair tasks
     * to this order, and changes its state to
     * {@link RepairOrderState#READY_FOR_APPROVAL}.
     *
     * @param diagnosticReport The diagnostic report that the technician
     *                         produced.
     */
    public void addDiagnosticReport(DiagnosticReport diagnosticReport) {
        this.diagnosticReport = diagnosticReport;
        this.state = RepairOrderState.READY_FOR_APPROVAL;
    }

    /**
     * Marks this repair order as accepted by the customer and asks the
     * specified printer to print this order. The order hands the printer a
     * data-only {@link RepairOrderDTO} snapshot; how the printout is
     * formatted is decided entirely by the printer, so the model contains
     * no presentation logic.
     *
     * @param printer The printer used to produce the paper copy of this order.
     */
    public void accept(Printer printer) {
        this.state = RepairOrderState.ACCEPTED;
        printer.printRepairOrder(toDTO());
    }

    /**
     * @return An immutable, data-only snapshot of the current state of this
     *         order. The snapshot is the only thing the view sees of the
     *         order, so the entity itself stays intact.
     */
    public RepairOrderDTO toDTO() {
        return new RepairOrderDTO(
                repairOrderId,
                customer,
                problemDescription,
                creationTime,
                state,
                diagnosticReport == null ? null : diagnosticReport.getDescription(),
                diagnosticReport == null
                        ? Collections.emptyList()
                        : diagnosticReport.getProposedTasks(),
                getTotalCost(),
                getEstimatedCompletionTime());
    }

    private Amount getTotalCost() {
        if (diagnosticReport == null) {
            return new Amount(0);
        }
        return diagnosticReport.getTotalCost();
    }

    private LocalDateTime getEstimatedCompletionTime() {
        return creationTime.plusDays(DEFAULT_REPAIR_DAYS);
    }
}
