package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.DelegateThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.DelegateTheme;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;
import pt.ul.fc.di.css.alunos.democracia.handlers.ChooseDelegateHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;

@DataJpaTest
public class ChooseDelegateUseCaseTest {

  @Autowired private TestEntityManager em;

  @Autowired private CitizenRepository citRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private DelegateThemeRepository dtRepository;

  private ChooseDelegateHandler chooseDelegateHandler;

  @BeforeEach
  public void init() {
    MockitoAnnotations.openMocks(this);
    CitizenCatalog citizenCatalog = new CitizenCatalog(citRepository);
    ThemeCatalog themeCatalog = new ThemeCatalog(themeRepository);
    DelegateThemeCatalog dtCatalog = new DelegateThemeCatalog(dtRepository);

    chooseDelegateHandler = new ChooseDelegateHandler(themeCatalog, dtCatalog, citizenCatalog);
  }

  @Test
  public void getDelegatesTest() {

    Theme theme1 = new Theme("theme1", null);
    Citizen c1 = new Citizen("Sara", 1);
    Delegate d1 = new Delegate("Paulo", 2);
    Delegate d2 = new Delegate("Paula", 3);

    em.persist(c1);
    em.persist(theme1);
    em.persist(d1);
    em.persist(d2);

    List<Delegate> delegates = new ArrayList<>();
    delegates.add(d1);
    delegates.add(d2);

    List<DelegateDTO> delegateDTOS = chooseDelegateHandler.getDelegates();

    for (int i = 0; i < delegates.size(); i++) {

      assertEquals(delegates.get(i).getName(), delegateDTOS.get(i).getName());
      assertEquals(delegates.get(i).getNif(), delegateDTOS.get(i).getNif());
    }
  }

  @Test
  public void chooseDelegateTest() {
    Citizen c1 = new Citizen("Sara", 1);
    Theme theme1 = new Theme("Saude", null);
    Theme theme2 = new Theme("Educacao", null);
    Delegate d1 = new Delegate("Paulo", 2);
    Delegate d2 = new Delegate("Paula", 3);

    em.persist(c1);
    em.persist(theme1);
    em.persist(theme2);
    em.persist(d1);
    em.persist(d2);

    List<ThemeDTO> themes = chooseDelegateHandler.getThemes();
    List<DelegateDTO> delegateDTOS = chooseDelegateHandler.getDelegates();

    for (int i = 0; i < themes.size(); i++) {
      chooseDelegateHandler.chooseDelegate(delegateDTOS.get(i), themes.get(i), c1.getNif());
    }

    List<DelegateTheme> dt_list = dtRepository.getAllDTs();

    for (DelegateTheme delegateTheme : dt_list) {

      System.out.println(
          "DT: "
              + delegateTheme.getDelegate().getName()
              + " "
              + delegateTheme.getTheme().getDesignation());

      for (int j = 0; j < delegateTheme.getVoters().size(); j++) {
        System.out.println("Voters: " + delegateTheme.getVoters().get(j).getName());
        assertEquals(delegateTheme.getVoters().get(j).getName(), c1.getName());
      }
    }

    Citizen c2 = new Citizen("Sarah", 4);
    em.persist(c2);
    chooseDelegateHandler.chooseDelegate(delegateDTOS.get(0), themes.get(0), c2.getNif());
    List<DelegateTheme> dt_list2 = dtRepository.getAllDTs();

    for (DelegateTheme delegateTheme : dt_list2) {
      System.out.println(
          "DT: "
              + delegateTheme.getDelegate().getName()
              + " "
              + delegateTheme.getTheme().getDesignation());
    }

    assertEquals(dt_list2.size(), 2);

    DelegateTheme dt = dt_list2.get(0);

    assertEquals(dt.getVoters().size(), 2);
  }
}
