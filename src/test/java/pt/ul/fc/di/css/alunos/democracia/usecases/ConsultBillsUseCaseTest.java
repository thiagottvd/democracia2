package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ConsultBillsService;

@DataJpaTest
public class ConsultBillsUseCaseTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  private BillCatalog billCatalog;
  private ConsultBillsHandler consultBillsHandler;
  private ConsultBillsService consultBillsService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    billCatalog = new BillCatalog(billRepository);
    consultBillsHandler = new ConsultBillsHandler(billCatalog);
    consultBillsService = new ConsultBillsService(consultBillsHandler);
  }

  @Test
  public void testGetOpenBills() throws IOException {
    // Create test data
    List<BillDTO> expectedBills = new ArrayList<>();

    Delegate delegate = new Delegate("A", 123);
    entityManager.persist(delegate);
    Delegate delegate2 = new Delegate("B", 444);
    entityManager.persist(delegate2);

    Theme theme = new Theme("C", null);
    entityManager.persist(theme);
    Theme theme2 = new Theme("D", theme);
    entityManager.persist(theme2);

    Bill bill = new Bill("E", "F", null, LocalDate.now().plusDays(1), delegate, theme);
    entityManager.persist(bill);
    Bill bill2 = new Bill("G", "H", null, LocalDate.now().plusDays(2), delegate2, theme2);
    entityManager.persist(bill2);

    BillDTO billDTO = new BillDTO(bill);
    BillDTO billDTO2 = new BillDTO(bill2);

    expectedBills.add(billDTO);
    expectedBills.add(billDTO2);

    // Call the method under test
    List<BillDTO> actualBills = consultBillsService.getOpenBills();

    // Verify the results
    assertEquals(expectedBills.size(), actualBills.size());

    for (int i = 0; i < expectedBills.size(); i++) {
      BillDTO expected = expectedBills.get(i);
      BillDTO actual = actualBills.get(i);

      assertEquals(expected.getId(), actual.getId());
      assertEquals(expected.getTitle(), actual.getTitle());
    }
  }

  @Test
  public void testGetBillDetails() throws ApplicationException, IOException {
    // Create temporary files
    File tempFile = File.createTempFile("test", ".pdf");
    FileWriter writer = new FileWriter(tempFile);
    writer.write("Hello, world!");
    writer.close();
    byte[] fileData = Files.readAllBytes(tempFile.toPath());

    Delegate delegate = new Delegate("Rodrigo T.", 123);
    entityManager.persist(delegate);

    Theme theme = new Theme("Saude", null);
    entityManager.persist(theme);

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

    BillDTO actualBill = consultBillsService.getBillDetails(expectedBill.getId());

    assertEquals(expectedBill.getId(), actualBill.getId());
    assertEquals(expectedBill.getTitle(), actualBill.getTitle());
    assertEquals(expectedBill.getDescription(), actualBill.getDescription());
    assertEquals(expectedBill.getNumSupporters(), actualBill.getNumSupporters());
    assertEquals(expectedBill.getExpirationDate(), actualBill.getExpirationDate());
    assertEquals(expectedBill.getTheme().getDesignation(), actualBill.getTheme());
    assertEquals(expectedBill.getDelegate().getName(), actualBill.getDelegate());
    assertArrayEquals(expectedBill.getFileData(), actualBill.getFileData());
  }
}
