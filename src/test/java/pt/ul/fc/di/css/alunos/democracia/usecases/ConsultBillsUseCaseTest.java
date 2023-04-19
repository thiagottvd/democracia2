package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;

/**
 * This class contains the test cases for the use case G. It contains tests for verifying the output
 * types and expected behavior of the use case G service class. It uses JUnit 5.
 */
@DataJpaTest
public class ConsultBillsUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  private BillCatalog billCatalog;
  private ConsultBillsHandler consultBillsHandler;
  private ConsultBillsService consultBillsService;

  /**
   * This method is called before each test case and initializes the dependencies required for the
   * tests. The BillCatalog and ConsultBillsHandler objects are instantiated and passed to a new
   * instance of the ConsultBillsService class.
   */
  @BeforeEach
  public void init() {
    billCatalog = new BillCatalog(billRepository);
    consultBillsHandler = new ConsultBillsHandler(billCatalog);
    consultBillsService = new ConsultBillsService(consultBillsHandler);
  }

  /** Test case to test the output type for getOpenBills method (should be BillDTO). */
  @Test
  public void testOutputTypeForGetOpenBills() {
    // Creating and persisting test data
    Delegate d = new Delegate("d", 1);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("b", "b", null, LocalDate.now(), d, t);
    entityManager.persist(b);

    // Getting the open bills (only one persisted)
    List<BillDTO> activePolls = consultBillsService.getOpenBills();

    // Verifying that the list is not empty
    assertFalse(activePolls.isEmpty());

    // Verifying that the first element is a BillDTO
    assertEquals(BillDTO.class, activePolls.get(0).getClass());
  }

  /** Test case to test the output type for getBillDetails method (should be BillDTO). */
  @Test
  public void testOutputTypeForGetBillDetails() throws ApplicationException {
    // Creating and persisting test data
    Delegate d = new Delegate("d", 1);
    entityManager.persist(d);
    Theme t = new Theme("t", null);
    entityManager.persist(t);
    Bill b = new Bill("b", "b", null, LocalDate.now(), d, t);
    entityManager.persist(b);

    // Call the method under test
    BillDTO bdto = consultBillsService.getBillDetails(b.getId());

    // Verifying if bdto is a BillDTO
    assertEquals(BillDTO.class, bdto.getClass());
  }

  /**
   * Tests the {@link ConsultBillsService#getOpenBills()} method to ensure that only open bills are
   * returned and that the returned {@link BillDTO} objects have the expected properties.
   */
  @Test
  public void testGetOpenBills() {
    // Initialize test data
    List<BillDTO> actualBillsDTO = new ArrayList<>();

    // Create and persisting 10 bills (5 OPEN and 5 CLOSED)
    for (int i = 0; i < 10; i++) {

      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);

      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);

      Bill b = new Bill(String.valueOf(i), String.valueOf(i), null, LocalDate.now(), d, t);
      entityManager.persist(b);

      // Set the status of even numbered bills to CLOSED and add the rest to the actualBillsDTO list
      if (i % 2 == 0) {
        b.setStatus(BillStatus.CLOSED);
      } else {
        actualBillsDTO.add(new BillDTO(b.getId(), b.getTitle()));
      }
    }

    // Test that open bills are correctly filtered
    List<BillDTO> expectedBills = consultBillsService.getOpenBills();
    assertEquals(5, expectedBills.size());

    // Check that all returned bills have an OPEN status
    for (BillDTO bill : expectedBills) {
      Bill b = billCatalog.getBill(bill.getId()).get();
      assertNotNull(b);
      assertEquals(BillStatus.OPEN, b.getStatus());
    }

    // Test that all properties of the returned BillDTO objects match the expected values
    for (int i = 0; i < expectedBills.size(); i++) {
      BillDTO expected = expectedBills.get(i);
      BillDTO actual = actualBillsDTO.get(i);

      // Check that the titles match
      assertEquals(expected.getTitle(), actual.getTitle());

      // Check that the IDs match
      assertEquals(expected.getId(), actual.getId());

      // Check that the descriptions are null
      assertNull(expected.getDescription());
      assertNull(actual.getDescription());

      // Check that the expiration dates are null
      assertNull(expected.getExpirationDate());
      assertNull(actual.getExpirationDate());
    }
  }

  /**
   * Tests that the getBillDetails() method throws an ApplicationException when the bill with the
   * given id no dot exists.
   */
  @Test
  public void testGetBillDetailsValidation() {
    assertThrows(ApplicationException.class, () -> consultBillsService.getBillDetails(-1L));
  }

  /**
   * Tests that the service returns the expected details for a bill.
   *
   * <p>This test creates a temporary file and uses its data to create a new Bill entity. It then
   * tests that the service returns a BillDTO object with the expected values for this Bill.
   *
   * @throws IOException if there is an error creating or reading the temporary file.
   * @throws ApplicationException if there is an error retrieving the bill details.
   */
  @Test
  public void testGetBillDetails() throws IOException, ApplicationException {
    // Create temporary file
    File tempFile = File.createTempFile("test", ".pdf");
    FileWriter writer = new FileWriter(tempFile);
    writer.write("Hello, world!");
    writer.close();
    byte[] fileData = Files.readAllBytes(tempFile.toPath());

    // Create a Delegate and Theme for the bill
    Delegate delegate = new Delegate("Rodrigo T.", 123);
    entityManager.persist(delegate);

    Theme theme = new Theme("Saude", null);
    entityManager.persist(theme);

    // Create the expected Bill and BillDTO objects
    Bill expectedBill =
        new Bill(
            "Compra vacinas covid",
            "Compra de vacinas pfizer",
            fileData,
            LocalDate.now().plusDays(1),
            delegate,
            theme);
    entityManager.persist(expectedBill);

    BillDTO expectedBillDTO = new BillDTO(expectedBill);

    // Test that the service returns the expected BillDTO object
    BillDTO actualBillDTO = consultBillsService.getBillDetails(expectedBill.getId());

    // Check that the returned BillDTO object has the expected values
    assertEquals(expectedBillDTO.getTitle(), actualBillDTO.getTitle());
    assertEquals(expectedBillDTO.getDescription(), actualBillDTO.getDescription());
    assertEquals(expectedBillDTO.getExpirationDate(), actualBillDTO.getExpirationDate());
    assertEquals(expectedBillDTO.getNumSupporters(), actualBillDTO.getNumSupporters());
    assertEquals(expectedBillDTO.getThemeDesignation(), actualBillDTO.getThemeDesignation());
    assertEquals(expectedBillDTO.getDelegateName(), actualBillDTO.getDelegateName());
    assertArrayEquals(expectedBillDTO.getFileData(), actualBillDTO.getFileData());
  }
}
