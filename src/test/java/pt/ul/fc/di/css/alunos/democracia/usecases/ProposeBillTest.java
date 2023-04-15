package pt.ul.fc.di.css.alunos.democracia.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.handlers.ProposeBillHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
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
    private BillCatalog billCatalog;
    private ThemeCatalog themeCatalog;
    private ProposeBillHandler proposeBillHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        billCatalog = new BillCatalog(billRepository);
        themeCatalog = new ThemeCatalog(themeRepository);
        proposeBillHandler = new ProposeBillHandler(themeCatalog, billCatalog);
    }

    @Test
    public void testProposeBill() {
        Theme theme1 = new Theme("theme1", null);
        Bill bill1 = new Bill("Bill1", "desc bill1", null, LocalDate.now(), null, null);
        entityManager.persist(theme1);
        entityManager.persist(bill1);

        Theme theme2 = new Theme("theme2", null);
        Bill bill2 = new Bill("Bill2", "desc bill2", null, LocalDate.now(), null, null);
        entityManager.persist(theme2);
        entityManager.persist(bill2);

        // Call the use case
        List<ThemeDTO> themesDTOList = proposeBillHandler.getThemes();
        proposeBillHandler.proposeBill("Bill3", "desc Bill3", null, LocalDate.now(), null, null);

        // Verify the results
        for(ThemeDTO theme : themesDTOList){
            System.out.println("Theme designation: " + theme.getDesignation());
        }
        assertEquals(2, themesDTOList.size());
        assertEquals(bill1.getTitle(), "Bill1");
    }
}