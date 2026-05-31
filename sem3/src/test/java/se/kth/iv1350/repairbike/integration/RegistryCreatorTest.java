package se.kth.iv1350.repairbike.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link RegistryCreator}.
 */
public class RegistryCreatorTest {
    private RegistryCreator creator;

    @BeforeEach
    public void setUp() {
        creator = new RegistryCreator();
    }

    @AfterEach
    public void tearDown() {
        creator = null;
    }

    @Test
    public void testGetCustomerRegistryReturnsNonNull() {
        assertNotNull(creator.getCustomerRegistry(),
                "getCustomerRegistry returned null.");
    }

    @Test
    public void testGetRepairOrderRegistryReturnsNonNull() {
        assertNotNull(creator.getRepairOrderRegistry(),
                "getRepairOrderRegistry returned null.");
    }

    @Test
    public void testGetCustomerRegistryReturnsSameInstanceOnRepeatedCalls() {
        CustomerRegistry first = creator.getCustomerRegistry();
        CustomerRegistry second = creator.getCustomerRegistry();
        assertSame(first, second,
                "getCustomerRegistry returned a different instance on the second call.");
    }

    @Test
    public void testGetRepairOrderRegistryReturnsSameInstanceOnRepeatedCalls() {
        RepairOrderRegistry first = creator.getRepairOrderRegistry();
        RepairOrderRegistry second = creator.getRepairOrderRegistry();
        assertSame(first, second,
                "getRepairOrderRegistry returned a different instance on the second call.");
    }
}
