# Repair Electric Bike — Seminar 3 (IV1350)

Java implementation of the basic flow for the *Repair Electric Bike* use case
that was analysed in seminar 1 and designed in seminar 2 of the KTH course
**IV1350 Object-Oriented Design**.

This repository is the deliverable for **Seminar 3, Implementation**.

## Project layout

```
src/
  main/java/se/kth/iv1350/repairbike/
    startup/      Main.java
    view/         View.java
    controller/   Controller.java
    model/        RepairOrder.java, DiagnosticReport.java,
                  RepairTask.java, Amount.java, RepairOrderState.java
    integration/  CustomerDTO.java, BikeDTO.java,
                  CustomerRegistry.java, RepairOrderRegistry.java,
                  RegistryCreator.java, Printer.java
  test/java/se/kth/iv1350/repairbike/
    controller/   ControllerTest.java
    model/        AmountTest.java, DiagnosticReportTest.java,
                  RepairOrderTest.java
    integration/  CustomerRegistryTest.java,
                  RepairOrderRegistryTest.java
report/
    seminar3-report.pdf   The IMRaD report.
    seminar3-report-updated.pdf    Updated IMRaD report.
    sample-run.txt         Captured System.out output of one full run.
pom.xml                    Maven build (Java 17 + JUnit 5).
```

## Building and running

Requires Java 17 (or later) and Apache Maven.

```bash
# Run the program
mvn -q exec:java

# Run the unit tests
mvn -q test
```

Or, without Maven, with a JDK on the PATH:

```bash
# Compile
javac -d out $(find src/main/java -name "*.java")

# Run
java -cp out se.kth.iv1350.repairbike.startup.Main
```

## What is implemented

The basic flow of the use case from seminar 1, that is:

1. The receptionist looks up a customer by phone number.
2. The customer's problem description is registered, which creates a new
   repair order in state `NEWLY_CREATED`.
3. The technician registers the diagnostic report and the proposed repair
   tasks, which moves the order to state `READY_FOR_APPROVAL`.
4. The receptionist registers that the customer accepts the proposed tasks,
   which moves the order to state `ACCEPTED` and triggers the printer.

The alternative flows (unknown phone number, repair order rejected) are
**not** implemented, since they were not required for seminar 3 and are best
expressed with the exception handling that is the topic of seminar 4.

## What is mocked

There is no real database, no real customer registry and no real printer.
The integration layer keeps a hard-coded list of customers in
`CustomerRegistry`, the repair orders are stored in memory in
`RepairOrderRegistry`, and the `Printer` simply forwards the printout to
`System.out`.

## Sample run

See `report/sample-run.txt` for the complete output of one full execution
of the basic flow.
