package pt.ul.fc.di.css.alunos.democracia.usecases;

import static org.junit.jupiter.api.Assertions.*;

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
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.DuplicateDelegateThemeException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ThemeNotFoundException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ChooseDelegateHandler;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.DelegateThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.repositories.ThemeRepository;
import pt.ul.fc.di.css.alunos.democracia.services.ChooseDelegateService;

/** ChooseDelegateUseCaseTest is a test class for the ChooseDelegateService class. */
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

  /** Tests the output if there is no themes and delegates in the database. */
  @Test
  public void emptyListsTest() {
    List<ThemeDTO> expectedThemes = chooseDelegateService.getThemes();
    List<DelegateDTO> expectedDelegates = chooseDelegateService.getDelegates();
    assertEquals(expectedThemes.size(), 0);
    assertEquals(expectedDelegates.size(), 0);
  }

  /** Tests getThemes method. */
  @Test
  public void getThemesTest() {
    List<ThemeDTO> expectedThemes = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      Theme theme = new Theme(String.valueOf(i), null);
      expectedThemes.add(new ThemeDTO(theme.getDesignation()));
      em.persist(theme);
    }

    List<ThemeDTO> actualThemes = chooseDelegateService.getThemes();

    assertEquals(expectedThemes.size(), actualThemes.size());

    for (int i = 0; i < actualThemes.size(); i++) {
      assertEquals(expectedThemes.get(i).getDesignation(), actualThemes.get(i).getDesignation());
    }
  }

  /** Tests getDelegates method. */
  @Test
  public void getDelegatesTest() {
    List<DelegateDTO> expectedDelegates = new ArrayList<>();

    for (int i = 0; i < 5; i++) {
      Delegate delegate = new Delegate(String.valueOf(i), i);
      expectedDelegates.add(new DelegateDTO(String.valueOf(i), i));
      em.persist(delegate);
    }

    List<DelegateDTO> actualDelegates = chooseDelegateService.getDelegates();

    assertEquals(expectedDelegates.size(), actualDelegates.size());

    for (int i = 0; i < actualDelegates.size(); i++) {
      assertEquals(expectedDelegates.get(i).getName(), actualDelegates.get(i).getName());
      assertEquals(
          expectedDelegates.get(i).getCitizenCardNumber(),
          actualDelegates.get(i).getCitizenCardNumber());
    }
  }

  /**
   * Test case to verify that a CitizenNotFoundException is thrown when the voter is not found.
   *
   * <p>This test case creates a delegate and a theme. It then attempts to choose a delegate for the
   * theme by specifying a voter citizen card number that does not exist in the database. The test
   * case expects a CitizenNotFoundException to be thrown.
   */
  @Test
  public void voterNotFoundExceptionTest() {
    Delegate delegate = new Delegate("delegate", 1);
    Theme theme = new Theme("theme", null);

    em.persist(delegate);
    em.persist(theme);

    assertThrows(
        CitizenNotFoundException.class,
        () ->
            chooseDelegateService.chooseDelegate(
                delegate.getCitizenCardNumber(), theme.getDesignation(), 99));
  }

  /**
   * Test case to verify that a ThemeNotFoundException is thrown when the specified theme is not
   * found.
   *
   * <p>This test case creates a delegate and a voter. It then attempts to choose a delegate for a
   * theme by specifying a theme designation that does not exist in the database. The test case
   * expects a ThemeNotFoundException to be thrown.
   */
  @Test
  public void themeNotFoundExceptionTest() {
    Delegate delegate = new Delegate("delegate", 1);
    Citizen voter = new Citizen("voter", 2);
    em.persist(delegate);
    em.persist(voter);

    assertThrows(
        ThemeNotFoundException.class,
        () ->
            chooseDelegateService.chooseDelegate(
                delegate.getCitizenCardNumber(), "null", voter.getCitizenCardNumber()));
  }

  /**
   * Test case to verify that a CitizenNotFoundException is thrown when the delegate is not found.
   *
   * <p>This test case creates a voter and a theme. It then attempts to choose a delegate, that does
   * not exist in the database, for the theme by specifying a voter citizen card number. The test
   * case expects a CitizenNotFoundException to be thrown.
   */
  @Test
  public void delegateNotFoundExceptionTest() {
    Theme theme = new Theme("theme", null);
    Citizen voter = new Citizen("voter", 2);

    em.persist(theme);
    em.persist(voter);

    assertThrows(
        CitizenNotFoundException.class,
        () ->
            chooseDelegateService.chooseDelegate(
                99, theme.getDesignation(), voter.getCitizenCardNumber()));
  }

  /**
   * Test case to verify the handling of DuplicateDelegateThemeException.
   *
   * <p>This test case creates two delegates, a theme, and a voter. It associates the first delegate
   * with the theme using the chooseDelegate() method. It then attempts to update the delegate for
   * the same theme by selecting the second delegate using the chooseDelegate() method. The test
   * case expects a DuplicateDelegateThemeException to be thrown, indicating that the voter already
   * has a delegate associated with the specified theme. It also verifies if the data, in the
   * database, associated with both operations are correct.
   */
  @Test
  public void duplicateDelegateThemeException() throws ApplicationException {
    Delegate delegate = new Delegate("delegate", 1);
    Delegate delegate2 = new Delegate("delegate", 10);
    Theme theme = new Theme("theme", null);
    Citizen voter = new Citizen("voter", 2);

    em.persist(delegate);
    em.persist(delegate2);
    em.persist(theme);
    em.persist(voter);

    // associates the citizen delegate (delegate) for the theme.
    chooseDelegateService.chooseDelegate(
        delegate.getCitizenCardNumber(), theme.getDesignation(), voter.getCitizenCardNumber());

    assertEquals(1, dtRepository.getAllDTs().size());
    assertEquals(
        1,
        citizenRepository
            .findByCitizenCardNumber(voter.getCitizenCardNumber())
            .get()
            .getDelegateThemes()
            .size());

    assertEquals(delegate, dtRepository.getAllDTs().get(0).getDelegate());
    assertEquals(voter, dtRepository.getAllDTs().get(0).getVoters().get(0));
    assertEquals(theme, dtRepository.getAllDTs().get(0).getTheme());
    assertEquals(
        dtRepository.getAllDTs().get(0),
        citizenRepository
            .findByCitizenCardNumber(voter.getCitizenCardNumber())
            .get()
            .getDelegateThemes()
            .get(0));

    // updates the citizen delegate (delegate2) associated for the theme
    assertThrows(
        DuplicateDelegateThemeException.class,
        () ->
            chooseDelegateService.chooseDelegate(
                delegate2.getCitizenCardNumber(),
                theme.getDesignation(),
                voter.getCitizenCardNumber()));

    assertEquals(1, dtRepository.getAllDTs().size());
    assertEquals(
        1,
        citizenRepository
            .findByCitizenCardNumber(voter.getCitizenCardNumber())
            .get()
            .getDelegateThemes()
            .size());

    assertEquals(delegate2, dtRepository.getAllDTs().get(0).getDelegate());
    assertEquals(voter, dtRepository.getAllDTs().get(0).getVoters().get(0));
    assertEquals(theme, dtRepository.getAllDTs().get(0).getTheme());
    assertEquals(
        dtRepository.getAllDTs().get(0),
        citizenRepository
            .findByCitizenCardNumber(voter.getCitizenCardNumber())
            .get()
            .getDelegateThemes()
            .get(0));
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
          delegateDTOS.get(i).getCitizenCardNumber(),
          themes.get(i).getDesignation(),
          c1.getCitizenCardNumber());
    }

    List<DelegateTheme> dt_list = dtRepository.getAllDTs();
    assertEquals(2, dt_list.size());

    // Checking persistence and connections
    Citizen c2 = new Citizen("Manuel", 5);
    Delegate d3 = new Delegate("Paulina", 4);
    Theme theme3 = new Theme("Climate", null);
    em.persist(d3);
    em.persist(theme3);
    em.persist(c2);

    chooseDelegateService.chooseDelegate(
        d3.getCitizenCardNumber(), theme3.getDesignation(), c1.getCitizenCardNumber());

    chooseDelegateService.chooseDelegate(
        d3.getCitizenCardNumber(), theme3.getDesignation(), c2.getCitizenCardNumber());

    // Should not create a new DT since there was already one with the same delegate and theme
    // Size of dtRepo must be 3 even tho there were 4 chooseDelegates that got called
    assertEquals(3, dtRepository.getAllDTs().size());
    assertEquals(
        3, citizenRepository.findByCitizenCardNumber(1).orElseThrow().getDelegateThemes().size());
    assertEquals(2, dtRepository.getAllDTs().get(2).getVoters().size());

    // Sara tem Paulo na Health/ Paula na Education / Paulina no Climate
    // Paulo em Saude
    assertEquals(
        d1.getCitizenCardNumber(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(0)
            .getDelegate()
            .getCitizenCardNumber());
    assertEquals(
        theme1.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(0)
            .getTheme()
            .getDesignation());

    // Paula em Educaçao
    assertEquals(
        d2.getCitizenCardNumber(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(1)
            .getDelegate()
            .getCitizenCardNumber());
    assertEquals(
        theme2.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(1)
            .getTheme()
            .getDesignation());

    // Paulina em Climate
    assertEquals(
        d3.getCitizenCardNumber(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(2)
            .getDelegate()
            .getCitizenCardNumber());
    assertEquals(
        theme3.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(1)
            .orElseThrow()
            .getDelegateThemes()
            .get(2)
            .getTheme()
            .getDesignation());

    // Checking same delegate different themes. Should be allowed
    Theme theme4 = new Theme("Economy", null);
    Theme theme5 = new Theme("Trade", null);
    em.persist(theme5);
    em.persist(theme4);

    chooseDelegateService.chooseDelegate(
        d3.getCitizenCardNumber(), theme4.getDesignation(), c2.getCitizenCardNumber());
    chooseDelegateService.chooseDelegate(
        d3.getCitizenCardNumber(), theme5.getDesignation(), c2.getCitizenCardNumber());
    assertEquals(5, dtRepository.getAllDTs().size());
    // Manuel has Paulina rep in Economy, Trade and Climate
    assertEquals(
        3, citizenRepository.findByCitizenCardNumber(5).orElseThrow().getDelegateThemes().size());
    assertEquals(
        d3.getCitizenCardNumber(),
        citizenRepository
            .findByCitizenCardNumber(5)
            .orElseThrow()
            .getDelegateThemes()
            .get(2)
            .getDelegate()
            .getCitizenCardNumber());
    assertEquals(
        theme3.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(5)
            .orElseThrow()
            .getDelegateThemes()
            .get(0)
            .getTheme()
            .getDesignation());
    assertEquals(
        theme4.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(5)
            .orElseThrow()
            .getDelegateThemes()
            .get(1)
            .getTheme()
            .getDesignation());
    assertEquals(
        theme5.getDesignation(),
        citizenRepository
            .findByCitizenCardNumber(5)
            .orElseThrow()
            .getDelegateThemes()
            .get(2)
            .getTheme()
            .getDesignation());

    // Checking same theme but different delegates. Only the new/last one should remain as
    // representative in that theme
    // Sara wants to get Paulina to replace Paulo in theme1 and Paula in theme2
    try {
      chooseDelegateService.chooseDelegate(
          d3.getCitizenCardNumber(), theme1.getDesignation(), c1.getCitizenCardNumber());
      chooseDelegateService.chooseDelegate(
          d3.getCitizenCardNumber(), theme2.getDesignation(), c1.getCitizenCardNumber());
    } catch (DuplicateDelegateThemeException e) {
      // Do nothing
    }

    assertEquals(5, dtRepository.getAllDTs().size());
    // Sara has Paulina in Health Education and Climate
    assertEquals(
        3, citizenRepository.findByCitizenCardNumber(1).orElseThrow().getDelegateThemes().size());
  }
}
