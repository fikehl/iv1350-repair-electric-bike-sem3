package se.kth.iv1350.repairbike.model;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import se.kth.iv1350.repairbike.integration.CustomerDTO;

/**
 * An immutable, data-only snapshot of a {@link RepairOrder}. Instances are
 * created by {@link RepairOrder#toDTO()} and are the only thing the view
 * sees of a repair order, so the model entity is kept intact. The DTO
 * carries data and nothing else; how the data is presented is decided by
 * whoever displays it.
 */
public final class RepairOrderDTO {
    private final int repairOrderId;
    private final CustomerDTO customer;
    private final String problemDescription;
    private final LocalDateTime creationTime;
    private final RepairOrderState state;
    private final String diagnosticDescription;
    private final List<RepairTask> proposedTasks;
    private final Amount totalCost;
    private final LocalDateTime estimatedCompletionTime;

    /**
     * Creates a new snapshot. Called only by {@link RepairOrder#toDTO()};
     * package-private so the view cannot fabricate snapshots.
     *
     * @param repairOrderId           The id of the snapshotted repair order.
     * @param customer                The customer that handed in the bike.
     * @param problemDescription      The customer's problem description.
     * @param creationTime            The time when the order was created.
     * @param state                   The current state.
     * @param diagnosticDescription   The technician's diagnostic findings, or
     *                                {@code null} if no diagnostic report has
     *                                been added yet.
     * @param proposedTasks           The proposed repair tasks; may be empty.
     * @param totalCost               The total cost of the proposed tasks.
     * @param estimatedCompletionTime The estimated completion time.
     */
    RepairOrderDTO(int repairOrderId,
                   CustomerDTO customer,
                   String problemDescription,
                   LocalDateTime creationTime,
                   RepairOrderState state,
                   String diagnosticDescription,
                   List<RepairTask> proposedTasks,
                   Amount totalCost,
                   LocalDateTime estimatedCompletionTime) {
        this.repairOrderId = repairOrderId;
        this.customer = customer;
        this.problemDescription = problemDescription;
        this.creationTime = creationTime;
        this.state = state;
        this.diagnosticDescription = diagnosticDescription;
        this.proposedTasks = Collections.unmodifiableList(proposedTasks);
        this.totalCost = totalCost;
        this.estimatedCompletionTime = estimatedCompletionTime;
    }

    /**
     * @return The id of the snapshotted repair order.
     */
    public int getRepairOrderId() {
        return repairOrderId;
    }

    /**
     * @return The customer that handed in the bike.
     */
    public CustomerDTO getCustomer() {
        return customer;
    }

    /**
     * @return The customer's problem description.
     */
    public String getProblemDescription() {
        return problemDescription;
    }

    /**
     * @return The time when the order was created.
     */
    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    /**
     * @return The state of the order at the time the snapshot was taken.
     */
    public RepairOrderState getState() {
        return state;
    }

    /**
     * @return The technician's diagnostic findings, or {@code null} if no
     *         diagnostic report has been added yet.
     */
    public String getDiagnosticDescription() {
        return diagnosticDescription;
    }

    /**
     * @return An unmodifiable view of the proposed repair tasks. The list is
     *         empty if no diagnostic report has been added.
     */
    public List<RepairTask> getProposedTasks() {
        return proposedTasks;
    }

    /**
     * @return The total cost of the proposed repair tasks.
     */
    public Amount getTotalCost() {
        return totalCost;
    }

    /**
     * @return The estimated completion time.
     */
    public LocalDateTime getEstimatedCompletionTime() {
        return estimatedCompletionTime;
    }
}
