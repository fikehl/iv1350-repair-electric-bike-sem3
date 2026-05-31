package se.kth.iv1350.repairbike.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
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
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

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
     * specified printer to print this order. Encapsulating the printer call
     * inside this method keeps the printing logic in the model, where it
     * belongs, so that the controller does not need to know how a repair
     * order is printed.
     *
     * @param printer The printer used to produce the paper copy of this order.
     */
    public void accept(Printer printer) {
        this.state = RepairOrderState.ACCEPTED;
        printer.printRepairOrder(createPrintout());
    }

    /**
     * @return An immutable snapshot of the current state of this order. The
     *         snapshot is the only thing the view sees of the order, so the
     *         entity itself stays intact.
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
                getEstimatedCompletionTime(),
                createPrintout());
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

    private String createPrintout() {
        StringBuilder builder = new StringBuilder();
        appendHeader(builder);
        appendCustomerSection(builder);
        appendProblemSection(builder);
        appendDiagnosticSection(builder);
        appendCompletionSection(builder);
        appendFooter(builder);
        return builder.toString();
    }

    private void appendHeader(StringBuilder builder) {
        builder.append("============================================\n");
        builder.append("           REPAIR ORDER #").append(repairOrderId).append("\n");
        builder.append("============================================\n");
        builder.append("Date:    ").append(creationTime.format(DATE_TIME_FORMATTER)).append("\n");
        builder.append("State:   ").append(state).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendCustomerSection(StringBuilder builder) {
        builder.append("Customer: ").append(customer.getName()).append("\n");
        builder.append("Phone:    ").append(customer.getPhoneNumber()).append("\n");
        builder.append("Email:    ").append(customer.getEmail()).append("\n");
        builder.append("Bike:     ")
               .append(customer.getBike().getBrand()).append(" ")
               .append(customer.getBike().getModel())
               .append(" (S/N ").append(customer.getBike().getSerialNumber()).append(")\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendProblemSection(StringBuilder builder) {
        builder.append("Customer's problem description:\n");
        builder.append("  ").append(problemDescription).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendDiagnosticSection(StringBuilder builder) {
        if (diagnosticReport == null) {
            builder.append("No diagnostic report has been added yet.\n");
            return;
        }
        builder.append("Diagnostic report:\n");
        builder.append("  ").append(diagnosticReport.getDescription()).append("\n");
        builder.append("\n");
        builder.append("Proposed repair tasks:\n");
        for (RepairTask task : diagnosticReport.getProposedTasks()) {
            builder.append("  - ").append(task).append("\n");
        }
        builder.append("\n");
        builder.append("Total cost: ").append(getTotalCost()).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendCompletionSection(StringBuilder builder) {
        builder.append("Estimated completion: ")
               .append(getEstimatedCompletionTime().format(DATE_TIME_FORMATTER))
               .append("\n");
    }

    private void appendFooter(StringBuilder builder) {
        builder.append("============================================\n");
    }
}
