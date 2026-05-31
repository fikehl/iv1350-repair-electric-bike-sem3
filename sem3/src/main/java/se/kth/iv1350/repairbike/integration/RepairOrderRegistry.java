package se.kth.iv1350.repairbike.integration;

import java.util.ArrayList;
import java.util.List;
import se.kth.iv1350.repairbike.model.RepairOrder;

/**
 * Contains all calls to the data store with repair orders. In this version of
 * the program no real database is used, instead the repair orders are kept in
 * memory. Repair orders are never deleted.
 */
public class RepairOrderRegistry {
    private final List<RepairOrder> repairOrders = new ArrayList<>();
    private int nextRepairOrderId = 1;

    /**
     * Creates a new instance.
     */
    RepairOrderRegistry() {
    }

    /**
     * Stores the specified repair order in the registry. The repair order
     * must not already be in the registry; use {@link #updateRepairOrder}
     * to persist subsequent changes.
     *
     * @param repairOrder The repair order to add.
     */
    public void addRepairOrder(RepairOrder repairOrder) {
        repairOrders.add(repairOrder);
    }

    /**
     * Persists the current state of the specified repair order. In this
     * in-memory implementation the registry already holds a reference to the
     * order, so the call is a no-op. The method exists to make the controller
     * code explicit about its intent and to mirror the signature a real
     * database-backed registry would need.
     *
     * @param repairOrder The repair order whose state shall be persisted.
     */
    public void updateRepairOrder(RepairOrder repairOrder) {
        // No-op: the in-memory list already holds the same reference. In a
        // real implementation this would write the updated state to the
        // database.
    }

    /**
     * Returns the next unused repair order id and reserves it. Calling this
     * method twice will never return the same id.
     *
     * @return The next unused repair order id.
     */
    public int nextRepairOrderId() {
        int reservedId = nextRepairOrderId;
        nextRepairOrderId++;
        return reservedId;
    }

    /**
     * @return The number of repair orders currently stored in the registry.
     */
    public int numberOfRepairOrders() {
        return repairOrders.size();
    }
}
