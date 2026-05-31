package se.kth.iv1350.repairbike.controller;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.repairbike.integration.CustomerDTO;
import se.kth.iv1350.repairbike.integration.Printer;
import se.kth.iv1350.repairbike.integration.RegistryCreator;
import se.kth.iv1350.repairbike.model.Amount;
import se.kth.iv1350.repairbike.model.RepairOrderDTO;
import se.kth.iv1350.repairbike.model.RepairOrderState;
import se.kth.iv1350.repairbike.model.RepairTask;

/**
 * Unit tests for {@link Controller}.
 */
public class ControllerTest {
    private static final String KNOWN_PHONE = "0701112233";
    private static final String UNKNOWN_PHONE = "0000000000";

    private Controller controller;

    @BeforeEach
    public void setUp() {
        controller = new Controller(new RegistryCreator(), new Printer());
    }

    @AfterEach
    public void tearDown() {
        controller = null;
    }

    @Test
    public void testSearchKnownCustomerReturnsCustomer() {
        CustomerDTO found = controller.searchCustomer(KNOWN_PHONE);
        assertNotNull(found,
                "searchCustomer returned null for a known phone number.");
        assertEquals(KNOWN_PHONE, found.getPhoneNumber(),
                "searchCustomer returned a customer with a different phone number.");
    }

    @Test
    public void testSearchUnknownCustomerReturnsNull() {
        CustomerDTO found = controller.searchCustomer(UNKNOWN_PHONE);
        assertNull(found,
                "searchCustomer returned a non-null result for an unknown phone number.");
    }

    @Test
    public void testRegisterProblemReturnsRepairOrderInNewlyCreatedState() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        RepairOrderDTO created = controller.registerProblem(customer, "Brakes squeaking.");
        assertNotNull(created, "registerProblem returned null.");
        assertEquals(RepairOrderState.NEWLY_CREATED, created.getState(),
                "Returned repair order was not in state NEWLY_CREATED.");
    }

    @Test
    public void testRegisterProblemStoresProblemDescription() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        String problem = "Battery dies after a few minutes.";
        RepairOrderDTO created = controller.registerProblem(customer, problem);
        assertEquals(problem, created.getProblemDescription(),
                "Problem description was not stored in the repair order.");
    }

    @Test
    public void testRegisterDiagnosticChangesStateToReadyForApproval() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        controller.registerProblem(customer, "Brakes squeaking.");
        RepairOrderDTO updated = controller.registerDiagnostic(
                "Brake pads worn down.", buildSampleTasks());
        assertEquals(RepairOrderState.READY_FOR_APPROVAL, updated.getState(),
                "State did not change to READY_FOR_APPROVAL after registerDiagnostic.");
    }

    @Test
    public void testRegisterDiagnosticUpdatesTotalCost() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        controller.registerProblem(customer, "Brakes squeaking.");
        RepairOrderDTO updated = controller.registerDiagnostic(
                "Brake pads worn down.", buildSampleTasks());
        Amount expected = new Amount(450 + 200);
        assertEquals(expected, updated.getTotalCost(),
                "Total cost did not match the sum of the registered repair tasks.");
    }

    @Test
    public void testAcceptRepairOrderChangesStateToAccepted() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        controller.registerProblem(customer, "Brakes squeaking.");
        controller.registerDiagnostic("Brake pads worn down.", buildSampleTasks());
        RepairOrderDTO accepted = controller.acceptRepairOrder();
        assertEquals(RepairOrderState.ACCEPTED, accepted.getState(),
                "State did not change to ACCEPTED after acceptRepairOrder.");
    }

    @Test
    public void testAcceptRepairOrderTriggersPrintout() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        controller.registerProblem(customer, "Brakes squeaking.");
        controller.registerDiagnostic("Brake pads worn down.", buildSampleTasks());
        PrintStream originalOut = System.out;
        ByteArrayOutputStream captured = new ByteArrayOutputStream();
        System.setOut(new PrintStream(captured));
        try {
            controller.acceptRepairOrder();
        } finally {
            System.setOut(originalOut);
        }
        String printed = captured.toString();
        assertTrue(printed.contains("REPAIR ORDER"),
                "Printer output did not contain a repair order header.");
        assertTrue(printed.contains("ACCEPTED"),
                "Printer output did not show that the order was ACCEPTED.");
    }

    @Test
    public void testRegisterProblemAndAcceptUseTheSameOrder() {
        CustomerDTO customer = controller.searchCustomer(KNOWN_PHONE);
        RepairOrderDTO created = controller.registerProblem(customer, "Brakes squeaking.");
        controller.registerDiagnostic("Brake pads worn down.", buildSampleTasks());
        PrintStream originalOut = System.out;
        System.setOut(new PrintStream(new ByteArrayOutputStream()));
        try {
            RepairOrderDTO accepted = controller.acceptRepairOrder();
            assertEquals(created.getRepairOrderId(), accepted.getRepairOrderId(),
                    "acceptRepairOrder returned an order with a different id than"
                    + " the one created by registerProblem.");
        } finally {
            System.setOut(originalOut);
        }
    }

    private List<RepairTask> buildSampleTasks() {
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace front brake pads", new Amount(450)));
        tasks.add(new RepairTask("Adjust gears", new Amount(200)));
        return tasks;
    }
}
