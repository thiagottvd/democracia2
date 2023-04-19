package pt.ul.fc.di.css.alunos.democracia.usecases;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.DelegateThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.PollCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.handlers.VoteActivePollsHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.PollRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.services.VoteActivePollsService;

@DataJpaTest
public class VoteActivePollsTest {

  @Autowired private TestEntityManager em;

  @Autowired private CitizenRepository citizenRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private DelegateThemeRepository dtRepository;
  @Autowired private PollRepository pollRepository;

  private VoteActivePollsHandler voteActivePollsHandler;
  private VoteActivePollsService voteActivePollsService;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    CitizenCatalog citizenCatalog = new CitizenCatalog(citizenRepository);
    ThemeCatalog themeCatalog = new ThemeCatalog(themeRepository);
    DelegateThemeCatalog dtCatalog = new DelegateThemeCatalog(dtRepository);
    PollCatalog pollCatalog = new PollCatalog(pollRepository);

    voteActivePollsHandler = new VoteActivePollsHandler(pollCatalog, citizenCatalog);
    voteActivePollsService = new VoteActivePollsService(voteActivePollsHandler);
  }
}
