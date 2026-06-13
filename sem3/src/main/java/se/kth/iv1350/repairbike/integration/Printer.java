package se.kth.iv1350.repairbike.integration;

import java.time.format.DateTimeFormatter;
import se.kth.iv1350.repairbike.model.RepairOrderDTO;
import se.kth.iv1350.repairbike.model.RepairTask;

/**
 * Represents the external printer that is used to produce a paper copy of
 * a repair order. In this version of the program no real printer is used,
 * instead the printout is sent to {@code System.out}.
 *
 * <p>The printer is given a {@link RepairOrderDTO}, which carries data only,
 * and renders the paper printout itself. How the printed page looks is a
 * concern of the printing device, so the rendering is encapsulated here and
 * kept out of the model, which therefore contains no presentation logic.</p>
 */
public class Printer {
    private static final DateTimeFormatter DATE_TIME_FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    /**
     * Creates a new instance.
     */
    public Printer() {
    }

    /**
     * Renders and prints a paper copy of the specified repair order.
     *
     * @param repairOrder A data-only snapshot of the repair order to print.
     */
    public void printRepairOrder(RepairOrderDTO repairOrder) {
        System.out.println(createPrintout(repairOrder));
    }

    private String createPrintout(RepairOrderDTO order) {
        StringBuilder builder = new StringBuilder();
        appendHeader(builder, order);
        appendCustomerSection(builder, order);
        appendProblemSection(builder, order);
        appendDiagnosticSection(builder, order);
        appendCompletionSection(builder, order);
        appendFooter(builder);
        return builder.toString();
    }

    private void appendHeader(StringBuilder builder, RepairOrderDTO order) {
        builder.append("============================================\n");
        builder.append("           REPAIR ORDER #").append(order.getRepairOrderId()).append("\n");
        builder.append("============================================\n");
        builder.append("Date:    ")
               .append(order.getCreationTime().format(DATE_TIME_FORMATTER)).append("\n");
        builder.append("State:   ").append(order.getState()).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendCustomerSection(StringBuilder builder, RepairOrderDTO order) {
        CustomerDTO customer = order.getCustomer();
        builder.append("Customer: ").append(customer.getName()).append("\n");
        builder.append("Phone:    ").append(customer.getPhoneNumber()).append("\n");
        builder.append("Email:    ").append(customer.getEmail()).append("\n");
        builder.append("Bike:     ")
               .append(customer.getBike().getBrand()).append(" ")
               .append(customer.getBike().getModel())
               .append(" (S/N ").append(customer.getBike().getSerialNumber()).append(")\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendProblemSection(StringBuilder builder, RepairOrderDTO order) {
        builder.append("Customer's problem description:\n");
        builder.append("  ").append(order.getProblemDescription()).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendDiagnosticSection(StringBuilder builder, RepairOrderDTO order) {
        if (order.getDiagnosticDescription() == null) {
            builder.append("No diagnostic report has been added yet.\n");
            return;
        }
        builder.append("Diagnostic report:\n");
        builder.append("  ").append(order.getDiagnosticDescription()).append("\n");
        builder.append("\n");
        builder.append("Proposed repair tasks:\n");
        for (RepairTask task : order.getProposedTasks()) {
            builder.append("  - ").append(task).append("\n");
        }
        builder.append("\n");
        builder.append("Total cost: ").append(order.getTotalCost()).append("\n");
        builder.append("--------------------------------------------\n");
    }

    private void appendCompletionSection(StringBuilder builder, RepairOrderDTO order) {
        builder.append("Estimated completion: ")
               .append(order.getEstimatedCompletionTime().format(DATE_TIME_FORMATTER))
               .append("\n");
    }

    private void appendFooter(StringBuilder builder) {
        builder.append("============================================\n");
    }
}
