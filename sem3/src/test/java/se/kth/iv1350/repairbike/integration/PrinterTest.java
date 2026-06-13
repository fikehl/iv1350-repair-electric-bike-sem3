package se.kth.iv1350.repairbike.integration;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import se.kth.iv1350.repairbike.model.Amount;
import se.kth.iv1350.repairbike.model.DiagnosticReport;
import se.kth.iv1350.repairbike.model.RepairOrder;
import se.kth.iv1350.repairbike.model.RepairTask;

/**
 * Unit tests for {@link Printer}. The printer writes to {@code System.out},
 * which is temporarily replaced with a {@link ByteArrayOutputStream} so the
 * tests can inspect the printed text. The tests verify that the rendered
 * printout contains the order's data, they do not test the exact format.
 */
public class PrinterTest {
    private Printer printer;
    private RepairOrder repairOrder;
    private PrintStream originalOut;
    private ByteArrayOutputStream capturedOutput;

    @BeforeEach
    public void setUp() {
        printer = new Printer();
        BikeDTO bike = new BikeDTO("Crescent", "Elina E8", "CR-E8-00417");
        CustomerDTO customer = new CustomerDTO("Anna Andersson", "0701112233",
                                               "anna@example.com", bike);
        repairOrder = new RepairOrder(42, customer, "Brakes squeaking.");
        originalOut = System.out;
        capturedOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(capturedOutput));
    }

    @AfterEach
    public void tearDown() {
        System.setOut(originalOut);
        printer = null;
        repairOrder = null;
        capturedOutput = null;
    }

    @Test
    public void testPrintoutContainsKeyInformation() {
        repairOrder.addDiagnosticReport(buildSampleReport());
        printer.printRepairOrder(repairOrder.toDTO());
        String printout = capturedOutput.toString();
        assertTrue(printout.contains("42"),
                "Printout did not contain the repair order id.");
        assertTrue(printout.contains("Anna Andersson"),
                "Printout did not contain the customer's name.");
        assertTrue(printout.contains("0701112233"),
                "Printout did not contain the customer's phone number.");
        assertTrue(printout.contains("Crescent"),
                "Printout did not contain the bike's brand.");
        assertTrue(printout.contains("Brakes squeaking."),
                "Printout did not contain the customer's problem description.");
        assertTrue(printout.contains("Total cost"),
                "Printout did not contain the total cost line.");
    }

    @Test
    public void testPrintoutMentionsMissingDiagnosticBeforeItIsAdded() {
        printer.printRepairOrder(repairOrder.toDTO());
        String printout = capturedOutput.toString();
        assertTrue(printout.toLowerCase().contains("no diagnostic"),
                "Printout did not indicate that no diagnostic report was added yet.");
    }

    private DiagnosticReport buildSampleReport() {
        List<RepairTask> tasks = new ArrayList<>();
        tasks.add(new RepairTask("Replace front brake pads", new Amount(450)));
        tasks.add(new RepairTask("Adjust gears", new Amount(200)));
        return new DiagnosticReport("Brakes worn, gears slip.", tasks);
    }
}
