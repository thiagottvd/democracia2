package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

@DataJpaTest
public class ProposeBillTest {

  @Autowired private TestEntityManager entityManager;
  @Autowired private BillRepository billRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private CitizenRepository citizenRepository;
  private BillCatalog billCatalog;
  private ThemeCatalog themeCatalog;
  private CitizenCatalog citizenCatalog;
  private ProposeBillHandler proposeBillHandler;
  private ConsultBillsHandler consultBillsHandler;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    billCatalog = new BillCatalog(billRepository);
    themeCatalog = new ThemeCatalog(themeRepository);
    citizenCatalog = new CitizenCatalog(citizenRepository);
    proposeBillHandler = new ProposeBillHandler(themeCatalog, billCatalog, citizenCatalog);
    consultBillsHandler = new ConsultBillsHandler(billCatalog);
  }

  @Test
  public void testProposeBill() throws ApplicationException {
    Theme theme1 = new Theme("theme1", null);
    Theme theme2 = new Theme("theme2", null);
    entityManager.persist(theme1);
    entityManager.persist(theme2);

    Delegate delegate1 = new Delegate("John", 12345);
    Delegate delegate2 = new Delegate("Diogo", 00000);
    entityManager.persist(delegate1);
    entityManager.persist(delegate2);

    // Call the use case
    List<ThemeDTO> themesDTOList = proposeBillHandler.getThemes();

    // Teste aceite
    proposeBillHandler.proposeBill(
        "Bill1", "desc Bill1", null, LocalDate.now(), theme1.getDesignation(), delegate1.getCc());
    // Teste sem delegate
    assertThrows(
        CitizenNotFoundException.class,
        () ->
            proposeBillHandler.proposeBill(
                "Bill2", "desc Bill2", null, LocalDate.now(), theme2.getDesignation(), -5));
    // Teste sem theme
    assertThrows(
        ThemeNotFoundException.class,
        () ->
            proposeBillHandler.proposeBill(
                "Bill3", "desc Bill3", null, LocalDate.now(), null, delegate1.getCc()));

    List<BillDTO> billDTOList = consultBillsHandler.getOpenBills();

    // Verify the results
    for (ThemeDTO theme : themesDTOList) {
      System.out.println("Theme designation: " + theme.getDesignation());
    }
    for (BillDTO bill : billDTOList) {
      System.out.println("Bill title: " + bill.getTitle());
    }
  }
}
