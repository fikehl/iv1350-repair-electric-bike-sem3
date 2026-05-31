package se.kth.iv1350.repairbike.integration;

import java.util.ArrayList;
import java.util.List;

/**
 * Contains all calls to the data store with customers and their bikes. In this
 * version of the program there is no real database, instead a hard-coded list
 * of customers is kept inside this class. The class would otherwise have called
 * an external customer registry to retrieve the data.
 */
public class CustomerRegistry {
    private final List<CustomerDTO> customers = new ArrayList<>();

    /**
     * Creates a new instance and populates the registry with hard-coded
     * customer data.
     */
    CustomerRegistry() {
        addHardcodedCustomers();
    }

    /**
     * Searches for the customer with the specified phone number.
     *
     * @param phoneNumber The phone number identifying the searched customer.
     * @return The customer matching the specified phone number, or {@code null}
     *         if no such customer was found.
     */
    public CustomerDTO findCustomer(String phoneNumber) {
        for (CustomerDTO customer : customers) {
            if (customer.getPhoneNumber().equals(phoneNumber)) {
                return customer;
            }
        }
        return null;
    }

    private void addHardcodedCustomers() {
        customers.add(new CustomerDTO(
                "Anna Andersson", "0701112233", "anna@example.com",
                new BikeDTO("Crescent", "Elina E8", "CR-E8-00417")));
        customers.add(new CustomerDTO(
                "Bertil Bengtsson", "0734445566", "bertil@example.com",
                new BikeDTO("Cube", "Reaction Hybrid", "CB-RH-92133")));
        customers.add(new CustomerDTO(
                "Cecilia Carlsson", "0767778899", "cecilia@example.com",
                new BikeDTO("Trek", "Verve+ 2", "TR-V2-55021")));
    }
}
