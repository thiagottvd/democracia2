package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

public class BillDTO {

  private String title;
  private LocalDate expirationDate;

  public BillDTO(Bill bill) {
    this.title = bill.getTitle();
    this.expirationDate = bill.getExpirationDate();
  }
}
