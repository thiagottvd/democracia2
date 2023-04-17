package pt.ul.fc.di.css.alunos.democracia.usecases;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
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
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class ProposeBillTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private BillRepository billRepository;
    @Autowired
    private ThemeRepository themeRepository;
    @Autowired
    private CitizenRepository citizenRepository;
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
    public void testProposeBill() {
        Theme theme1 = new Theme("theme1", null);
        Theme theme2 = new Theme("theme2", null);
        entityManager.persist(theme1);
        entityManager.persist(theme2);

        Citizen delegate1 = new Citizen("John", 12345);
        Citizen delegate2 = new Citizen("Diogo", 00000);
        entityManager.persist(delegate1);
        entityManager.persist(delegate2);

        // Call the use case
        List<ThemeDTO> themesDTOList = proposeBillHandler.getThemes();
        // Just for now Im gonna access the list with the right index cuz there's only 2 themes

        // Teste aceite
        proposeBillHandler.proposeBill("Bill1", "desc Bill1", null, LocalDate.now(), themesDTOList.get(0), new DelegateDTO(delegate1.getName(), delegate1.getNif()));
        // Teste sem delegate
        proposeBillHandler.proposeBill("Bill2", "desc Bill2", null, LocalDate.now(), themesDTOList.get(1), null);
        // Teste sem theme
        proposeBillHandler.proposeBill("Bill3", "desc Bill3", null, LocalDate.now(), null, new DelegateDTO(delegate2.getName(), delegate2.getNif()));

        List<BillDTO> billDTOList = consultBillsHandler.getOpenBills();

        // Verify the results
        for(ThemeDTO theme : themesDTOList){
            System.out.println("Theme designation: " + theme.getDesignation());
        }
        for(BillDTO bill : billDTOList){
            System.out.println("Bill title: " + bill.getTitle());
        }
    }
}