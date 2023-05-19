package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ProposeBillService;

/**
 * The ProposeBillUseCaseTest class is a JUnit test case that tests the ProposeBillService class
 * which is used to propose new bills. This class has several test cases to test different
 * functionality of the /ProposeBillService class such as: testOutputTypeGetThemes() - test the
 * output type of the getThemes() method. testThereIsNoThemes() - test the behavior when there are
 * no themes. testGetThemes() - test the getThemes() method to ensure that all persisted themes are
 * returned properly. testProposeBillInputValidation() - test the input validation of the
 * proposeBill() method.
 */
@DataJpaTest
public class ProposeBillUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private CitizenRepository citizenRepository;
  private BillCatalog billCatalog;
  private ThemeCatalog themeCatalog;
  private CitizenCatalog citizenCatalog;
  private ProposeBillService proposeBillService;

  /**
   * Initializes the ProposeBillTest class by instantiating the BillCatalog, ThemeCatalog,
   * CitizenCatalog, ProposeBillHandler, and ProposeBillService.
   */
  @BeforeEach
  public void init() {
    billCatalog = new BillCatalog(billRepository);
    themeCatalog = new ThemeCatalog(themeRepository);
    citizenCatalog = new CitizenCatalog(citizenRepository);
    ProposeBillHandler proposeBillHandler =
        new ProposeBillHandler(themeCatalog, billCatalog, citizenCatalog);
    proposeBillService = new ProposeBillService(proposeBillHandler);
  }

  /** Test case to test the output type of the method getThemes (should be ThemeDTO). */
  @Test
  public void testOutputTypeGetThemes() {
    // Creating and persisting test data
    Theme t = new Theme("t", null);
    entityManager.persist(t);

    // Getting the themes list
    List<ThemeDTO> themes = proposeBillService.getThemes();

    // Verifying that the list is not empty
    assertFalse(themes.isEmpty());

    // Verifying that the first element is a PollDTO
    assertEquals(ThemeDTO.class, themes.get(0).getClass());
  }

  /** Test case to test the system behaviour when there is no themes. */
  @Test
  public void testThereIsNoThemes() {
    // Given an empty database

    // When calling the method to retrieve all themes
    List<ThemeDTO> themes = proposeBillService.getThemes();

    // Then the returned list should be empty
    assertEquals(0, themes.size());
  }

  /**
   * This test method verifies the functionality of the getThemes() method of the ProposeBillService
   * class. It performs the following steps: Creates 50 themes using a for loop and persists them
   * using EntityManager. Calls the getThemes() method of ProposeBillService. Verifies that the
   * returned list of ThemeDTOs has a size of 50. This test ensures that the getThemes() method
   * properly retrieves all persisted themes and returns them as a list of ThemeDTOs.
   */
  @Test
  public void testGetThemes() {
    // Loop through 50 times to create and persist test data
    for (int i = 0; i < 50; i++) {
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
    }

    // Call the getThemes method
    List<ThemeDTO> themes = proposeBillService.getThemes();

    // Verify the results
    // Ensure that there are 50 themes returned
    assertEquals(50, themes.size());
  }

  /**
   * Tests if a delegate can propose multiple bills.
   *
   * @throws ApplicationException if any problems happens while proposing a bill
   */
  @Test
  public void proposeMultipleBills() throws ApplicationException {
    Delegate proposer = entityManager.persist(new Delegate("proposer", 52));

    // delegate 'proposer' is proposes multiple bills.
    for (int i = 0; i < 5; i++) {
      Theme theme = new Theme(String.valueOf(i), null);
      entityManager.persist(theme);
      proposeBillService.proposeBill(
          String.valueOf(i),
          String.valueOf(i),
          null,
          LocalDate.now(),
          theme.getDesignation(),
          proposer.getCc());
    }

    // checking if everything worked as supposed
    List<Bill> openBills = billCatalog.getOpenBills();
    assertEquals(5, openBills.size());

    for (Bill b : openBills) {
      assertEquals(1, b.getNumSupporters());
      assertEquals(proposer, b.getDelegate());
    }
  }

  /**
   * This test method verifies the input validation of the proposeBill() method of the
   * ProposeBillService class. It performs the following steps: Creates a new theme and persists it
   * using EntityManager. Calls the proposeBill() method of ProposeBillService with only the theme
   * designation parameter and expects a CitizenNotFoundException to be thrown. Deletes the theme
   * created in step 1 and creates a new delegate, persisting it using EntityManager. Calls the
   * proposeBill() method of ProposeBillService with only the delegate cc number parameter and
   * expects a ThemeNotFoundException to be thrown. This test ensures that the proposeBill() method
   * properly validates its input parameters and throws the appropriate exceptions when necessary.
   */
  @Test
  public void testProposeBillInputValidation() {
    // creating a theme
    Theme t = new Theme("t", null);
    entityManager.persist(t);

    // call the proposeBill method given only the theme designation (should throw
    // CitizenNotFoundException)
    assertThrows(
        CitizenNotFoundException.class,
        () ->
            proposeBillService.proposeBill("b", "b", null, LocalDate.now(), t.getDesignation(), 1));

    // deleting theme and creating a delegate
    entityManager.remove(t);
    Delegate d = new Delegate("d", 1);
    entityManager.persist(d);

    // call the proposeBill method given only the delegate cc number (should throw
    // ThemeNotFoundException)
    assertThrows(
        ThemeNotFoundException.class,
        () -> proposeBillService.proposeBill("b", "b", null, LocalDate.now(), null, d.getCc()));
  }

  /**
   * Tests the proposeBill() method of the ProposeBillService by creating and persisting necessary
   * data and calling the method 50 times, then verifying the results.
   *
   * <p>Specifically, this test verifies that 50 bills are added to the system, and that the title,
   * description, file data, expiration date, theme, and delegate of each bill are correctly set.
   *
   * @throws ApplicationException if any problems happens while proposing a bill
   */
  @Test
  public void testProposeBill() throws ApplicationException {

    // Create a local date object for testing purposes
    LocalDate localDate = LocalDate.now();

    // Create and persist necessary data, and call the proposeBill method 50 times
    for (int i = 0; i < 50; i++) {
      Theme t = new Theme(String.valueOf(i), null);
      entityManager.persist(t);
      Delegate d = new Delegate(String.valueOf(i), i);
      entityManager.persist(d);

      // Call the proposeBill method with the unique parameters for this iteration of the loop
      proposeBillService.proposeBill(
          String.valueOf(i),
          String.valueOf(i),
          String.valueOf(i).getBytes(),
          localDate,
          t.getDesignation(),
          d.getCc());
    }

    // Verify that 50 bills were added to the system
    List<Bill> bills = billCatalog.getOpenBills();
    assertEquals(50, bills.size());

    // Verify the data for each bill in the system
    for (int i = 0; i < 50; i++) {
      Bill b = bills.get(i);
      assertEquals(String.valueOf(i), b.getTitle());
      assertEquals(String.valueOf(i), b.getDescription());
      assertArrayEquals(String.valueOf(i).getBytes(), b.getFileData());
      assertEquals(localDate, b.getExpirationDate());
      assertEquals(themeCatalog.getTheme(String.valueOf(i)), b.getTheme());
      Citizen expectedDelegate = citizenCatalog.getCitizenByCc(i).orElse(null);
      assertEquals(expectedDelegate, b.getDelegate());
    }
  }
}
