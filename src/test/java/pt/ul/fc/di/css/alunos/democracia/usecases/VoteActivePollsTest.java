package pt.ul.fc.di.css.alunos.democracia.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;
import pt.ul.fc.di.css.alunos.democracia.handlers.VoteActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

@DataJpaTest
public class VoteActivePollsTest {
    @Autowired private TestEntityManager entityManager;
    @Autowired private BillRepository billRepository;
    @Autowired private ThemeRepository themeRepository;
    @Autowired private CitizenRepository citizenRepository;
    @Autowired private PollRepository pollRepository;
    private PollCatalog pollCatalog;
    private BillCatalog billCatalog;
    private ThemeCatalog themeCatalog;
    private CitizenCatalog citizenCatalog;
    private VoteActivePollsHandler voteActivePollsHandler;

    @BeforeEach
    public void init() {
        MockitoAnnotations.openMocks(this);
        pollCatalog = new PollCatalog(pollRepository);
        billCatalog = new BillCatalog(billRepository);
        themeCatalog = new ThemeCatalog(themeRepository);
        citizenCatalog = new CitizenCatalog(citizenRepository);
        voteActivePollsHandler = new VoteActivePollsHandler(pollCatalog, citizenCatalog);
    }

    @Test
    public void testProposeBill() {

    }
}
