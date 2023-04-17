package pt.ul.fc.di.css.alunos.democracia.handlers;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.catalogs.BillCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.CitizenCatalog;
import pt.ul.fc.di.css.alunos.democracia.catalogs.ThemeCatalog;
import pt.ul.fc.di.css.alunos.democracia.dtos.DelegateDTO;
import pt.ul.fc.di.css.alunos.democracia.dtos.ThemeDTO;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

@Component
public class ProposeBillHandler {

  private final ThemeCatalog themeCatalog;
  private final BillCatalog billCatalog;
  private final CitizenCatalog citizenCatalog;

  @Autowired
  public ProposeBillHandler(
      ThemeCatalog themeCatalog, BillCatalog billCatalog, CitizenCatalog citizenCatalog) {
    this.themeCatalog = themeCatalog;
    this.billCatalog = billCatalog;
    this.citizenCatalog = citizenCatalog;
  }

  /*
     Returns a list of all available themes.
  */
  public List<ThemeDTO> getThemes() {
    List<Theme> themes = themeCatalog.getThemes();
    return themes.stream()
        .map(theme -> new ThemeDTO(theme.getDesignation()))
        .collect(Collectors.toList());
  }

  /*
     Creates a new bill and adds it to the open bills list.
     @param title The new bill title.
     @param description The new bill description.
     @param pdf The new bill pdf file with the main content.
     @param expirationDate The new bill expiration date.
     @param delegate The new bill delegate.
     @param theme The new bill theme.
  */
  public void proposeBill(
      String title,
      String description,
      byte[] pdf,
      LocalDate expirationDate,
      ThemeDTO themeDTO,
      DelegateDTO delegateDTO) {
    if (delegateDTO == null) {
      System.out.println("No Delegate found.");
    } else if (themeDTO == null) {
      System.out.println("No corresponding Theme found.");
    } else {
      Delegate delegate = citizenCatalog.getDelegate(delegateDTO.getCc());
      Theme theme = themeCatalog.getTheme(themeDTO.getDesignation());
      billCatalog.addBill(new Bill(title, description, pdf, expirationDate, delegate, theme));
    }
  }
}
