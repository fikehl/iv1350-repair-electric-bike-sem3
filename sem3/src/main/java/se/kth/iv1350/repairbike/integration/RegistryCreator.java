package se.kth.iv1350.repairbike.integration;

/**
 * This class is responsible for instantiating all registries in the
 * integration layer. The same instances are shared by every caller asking for
 * a registry.
 */
public class RegistryCreator {
    private final CustomerRegistry customerRegistry = new CustomerRegistry();
    private final RepairOrderRegistry repairOrderRegistry = new RepairOrderRegistry();

    /**
     * Creates a new instance and instantiates all registries.
     */
    public RegistryCreator() {
    }

    /**
     * @return The single instance of the customer registry.
     */
    public CustomerRegistry getCustomerRegistry() {
        return customerRegistry;
    }

    /**
     * @return The single instance of the repair order registry.
     */
    public RepairOrderRegistry getRepairOrderRegistry() {
        return repairOrderRegistry;
    }
}
