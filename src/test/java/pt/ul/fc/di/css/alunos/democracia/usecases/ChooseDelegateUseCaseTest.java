package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ChooseDelegateHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ChooseDelegateService;

@DataJpaTest
public class ChooseDelegateUseCaseTest {

  @Autowired private TestEntityManager em;

  @Autowired private CitizenRepository citizenRepository;
  @Autowired private ThemeRepository themeRepository;
  @Autowired private DelegateThemeRepository dtRepository;

  private ChooseDelegateService chooseDelegateService;

  /**
   * Initializes the ChooseDelegateService with the necessary dependencies. The method creates
   * instances of CitizenCatalog, ThemeCatalog and DelegateThemeCatalog classes using the specified
   * citizenRepository, themeRepository, and dtRepository respectively. Then, it creates a
   * ChooseDelegateHandler instance using the created instances of ThemeCatalog,
   * DelegateThemeCatalog, and CitizenCatalog classes. Finally, it initializes the
   * ChooseDelegateService using the ChooseDelegateHandler instance.
   */
  @BeforeEach
  public void init() {
    CitizenCatalog citizenCatalog = new CitizenCatalog(citizenRepository);
    ThemeCatalog themeCatalog = new ThemeCatalog(themeRepository);
    DelegateThemeCatalog dtCatalog = new DelegateThemeCatalog(dtRepository);

    ChooseDelegateHandler chooseDelegateHandler =
        new ChooseDelegateHandler(themeCatalog, dtCatalog, citizenCatalog);
    chooseDelegateService = new ChooseDelegateService(chooseDelegateHandler);
  }

  /** Tests getDelegates method. */
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

    List<DelegateDTO> delegateDTOS = chooseDelegateService.getDelegates();

    for (int i = 0; i < delegates.size(); i++) {
      assertEquals(delegates.get(i).getName(), delegateDTOS.get(i).getName());
      assertEquals(delegates.get(i).getCc(), delegateDTOS.get(i).getCc());
    }
  }

  /**
   * Test equals method from Citizen and DelegateTheme classes.
   *
   * @throws ApplicationException if an error occurs while testing.
   */
  @Test
  public void equalsTest() throws ApplicationException {
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

    List<ThemeDTO> themes = chooseDelegateService.getThemes();
    List<DelegateDTO> delegateDTOS = chooseDelegateService.getDelegates();

    for (int i = 0; i < themes.size(); i++) {
      chooseDelegateService.chooseDelegate(
          delegateDTOS.get(i).getCc(), themes.get(i).getDesignation(), c1.getCc());
    }

    List<DelegateTheme> dt_list = dtRepository.getAllDTs();
    assertEquals(2, dt_list.size());

    Citizen c = citizenRepository.findByCc(1).orElse(null);
    assertNotNull(c);
    DelegateTheme dt = c.getDelegateThemes().get(0);

    // Testing equals method
    assertEquals(c1, c);
    assertEquals(dt_list.get(0), dt);
    assertEquals(dt_list.get(0), dtRepository.getDT(dt_list.get(0).getId()));
  }

  /**
   * Tests chooseDelegate method.
   *
   * @throws ApplicationException if an error occurs while testing the method.
   */
  @Test
  public void chooseDelegateTest() throws ApplicationException {
    Citizen c1 = new Citizen("Sara", 1);

    Theme theme1 = new Theme("Health", null);
    Theme theme2 = new Theme("Education", null);
    Delegate d1 = new Delegate("Paulo", 2);
    Delegate d2 = new Delegate("Paula", 3);

    em.persist(c1);
    em.persist(theme1);
    em.persist(theme2);
    em.persist(d1);
    em.persist(d2);

    // Checking DTO transfer
    List<ThemeDTO> themes = chooseDelegateService.getThemes();
    List<DelegateDTO> delegateDTOS = chooseDelegateService.getDelegates();

    for (int i = 0; i < themes.size(); i++) {
      chooseDelegateService.chooseDelegate(
          delegateDTOS.get(i).getCc(), themes.get(i).getDesignation(), c1.getCc());
    }

    List<DelegateTheme> dt_list = dtRepository.getAllDTs();
    assertEquals(2, dt_list.size());

    // Checking persistance and connections
    Citizen c2 = new Citizen("Manuel", 5);
    Delegate d3 = new Delegate("Paulina", 4);
    Theme theme3 = new Theme("Climate", null);
    em.persist(d3);
    em.persist(theme3);
    em.persist(c2);

    chooseDelegateService.chooseDelegate(d3.getCc(), theme3.getDesignation(), c1.getCc());

    chooseDelegateService.chooseDelegate(d3.getCc(), theme3.getDesignation(), c2.getCc());

    // Should not create a new DT since there was already one with the same delegate and theme
    // Size of dtRepo must be 3 even tho there where 4 chooseDelegates that got called
    assertEquals(3, dtRepository.getAllDTs().size());
    assertEquals(3, citizenRepository.findByCc(1).get().getDelegateThemes().size());
    assertEquals(2, dtRepository.getAllDTs().get(2).getVoters().size());

    // Sara tem Paulo na Health/ Paula na Education / Paulina no Climate
    // Paulo em Saude
    assertEquals(
        d1.getCc(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(0).getDelegate().getCc());
    assertEquals(
        theme1.getDesignation(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(0).getTheme().getDesignation());

    // Paula em Educaçao
    assertEquals(
        d2.getCc(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(1).getDelegate().getCc());
    assertEquals(
        theme2.getDesignation(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(1).getTheme().getDesignation());

    // Paulina em Climate
    assertEquals(
        d3.getCc(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(2).getDelegate().getCc());
    assertEquals(
        theme3.getDesignation(),
        citizenRepository.findByCc(1).get().getDelegateThemes().get(2).getTheme().getDesignation());

    // Checking same delegate different themes. Should be allowed
    Theme theme4 = new Theme("Economy", null);
    Theme theme5 = new Theme("Trade", null);
    em.persist(theme5);
    em.persist(theme4);

    chooseDelegateService.chooseDelegate(d3.getCc(), theme4.getDesignation(), c2.getCc());
    chooseDelegateService.chooseDelegate(d3.getCc(), theme5.getDesignation(), c2.getCc());
    assertEquals(5, dtRepository.getAllDTs().size());
    // Manuel has Paulina rep in Economy, Trade and Climate
    assertEquals(3, citizenRepository.findByCc(5).get().getDelegateThemes().size());
    assertEquals(
        d3.getCc(),
        citizenRepository.findByCc(5).get().getDelegateThemes().get(2).getDelegate().getCc());
    assertEquals(
        theme3.getDesignation(),
        citizenRepository.findByCc(5).get().getDelegateThemes().get(0).getTheme().getDesignation());
    assertEquals(
        theme4.getDesignation(),
        citizenRepository.findByCc(5).get().getDelegateThemes().get(1).getTheme().getDesignation());
    assertEquals(
        theme5.getDesignation(),
        citizenRepository.findByCc(5).get().getDelegateThemes().get(2).getTheme().getDesignation());

    // Checking same theme but different delegates. Only the new/last one should remain as
    // representative in that theme
    // Sara wants to get Paulina to replace Paulo in theme1 and Paula in theme2
    chooseDelegateService.chooseDelegate(d3.getCc(), theme1.getDesignation(), c1.getCc());
    chooseDelegateService.chooseDelegate(d3.getCc(), theme2.getDesignation(), c1.getCc());
    assertEquals(7, dtRepository.getAllDTs().size());
    // Sara has Paulina in Health Education and Climate
    assertEquals(3, citizenRepository.findByCc(1).get().getDelegateThemes().size());
    // Paulo in Health and Paula in Education do not have anymore citizens to represent.
    assertEquals(0, dtRepository.getAllDTs().get(0).getVoters().size());
    assertEquals(0, dtRepository.getAllDTs().get(1).getVoters().size());
  }
}
