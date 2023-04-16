package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.entities.Theme;

public class BillDTO {

  private String title;
  private LocalDate expirationDate;
  private Theme theme;

  public BillDTO(String title) {
    this.title = title;
  }

  public BillDTO(Bill bill) {
    this.title = bill.getTitle();
    this.expirationDate = bill.getExpirationDate();
    this.theme = bill.getTheme();
  }
}
