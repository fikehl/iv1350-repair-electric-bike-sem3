package se.kth.iv1350.repairbike.integration;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for {@link CustomerRegistry}. The registry is populated with
 * hard-coded customers, so the tests rely on the same data being present.
 */
public class CustomerRegistryTest {
    private CustomerRegistry registry;

    @BeforeEach
    public void setUp() {
        registry = new RegistryCreator().getCustomerRegistry();
    }

    @AfterEach
    public void tearDown() {
        registry = null;
    }

    @Test
    public void testFindExistingCustomerReturnsMatchingCustomer() {
        CustomerDTO found = registry.findCustomer("0701112233");
        assertNotNull(found,
                "findCustomer returned null for a known phone number.");
        assertEquals("0701112233", found.getPhoneNumber(),
                "findCustomer returned a customer with a different phone number.");
    }

    @Test
    public void testFindExistingCustomerReturnsAttachedBike() {
        CustomerDTO found = registry.findCustomer("0701112233");
        assertNotNull(found.getBike(),
                "Returned customer had no bike attached.");
        assertNotNull(found.getBike().getSerialNumber(),
                "Returned bike had no serial number.");
    }

    @Test
    public void testFindUnknownCustomerReturnsNull() {
        CustomerDTO found = registry.findCustomer("0000000000");
        assertNull(found,
                "findCustomer returned a customer for an unknown phone number.");
    }

    @Test
    public void testFindWithDifferentFormattedNumberReturnsNull() {
        CustomerDTO found = registry.findCustomer("070-111 22 33");
        assertNull(found,
                "findCustomer treated a differently formatted phone number as a match,"
                + " but it should match the stored format exactly.");
    }
}
