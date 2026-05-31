package se.kth.iv1350.repairbike.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.repairbike.model.RepairOrder;

/**
 * Unit tests for {@link RepairOrderRegistry}.
 */
public class RepairOrderRegistryTest {
    private RepairOrderRegistry registry;
    private CustomerDTO customer;

    @BeforeEach
    public void setUp() {
        registry = new RegistryCreator().getRepairOrderRegistry();
        customer = new CustomerDTO("Test Person", "0700000000",
                                   "test@example.com",
                                   new BikeDTO("TestBrand", "TestModel", "T-001"));
    }

    @AfterEach
    public void tearDown() {
        registry = null;
        customer = null;
    }

    @Test
    public void testNewRegistryIsEmpty() {
        assertEquals(0, registry.numberOfRepairOrders(),
                "A new RepairOrderRegistry was not empty.");
    }

    @Test
    public void testAddingRepairOrderIncreasesCount() {
        RepairOrder repairOrder = new RepairOrder(
                registry.nextRepairOrderId(), customer, "Squeaking brakes.");
        registry.addRepairOrder(repairOrder);
        assertEquals(1, registry.numberOfRepairOrders(),
                "Number of repair orders did not increase after adding one.");
    }

    @Test
    public void testUpdateRepairOrderDoesNotIncreaseCount() {
        RepairOrder repairOrder = new RepairOrder(
                registry.nextRepairOrderId(), customer, "Squeaking brakes.");
        registry.addRepairOrder(repairOrder);
        registry.updateRepairOrder(repairOrder);
        assertEquals(1, registry.numberOfRepairOrders(),
                "updateRepairOrder must not add a duplicate entry.");
    }

    @Test
    public void testNextRepairOrderIdProducesUniqueIds() {
        int firstId = registry.nextRepairOrderId();
        int secondId = registry.nextRepairOrderId();
        assertNotEquals(firstId, secondId,
                "nextRepairOrderId returned the same id twice.");
    }

    @Test
    public void testNextRepairOrderIdIsMonotonicallyIncreasing() {
        int firstId = registry.nextRepairOrderId();
        int secondId = registry.nextRepairOrderId();
        int thirdId = registry.nextRepairOrderId();
        assertTrue(secondId > firstId && thirdId > secondId,
                "nextRepairOrderId did not return monotonically increasing ids.");
    }
}
