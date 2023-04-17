package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

/** A Data Transfer Object (DTO) representing a bill. */
public class BillDTO {

  private Long id;
  private final String title;
  private String description;
  private LocalDate expirationDate;

  public BillDTO(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  /**
   * Constructs a BillDTO object with the given ID and title.
   *
   * @param title the title of the bill.
   */
  public BillDTO(String title, String description, LocalDate expirationDate) {
    this.title = title;
    this.description = description;
    this.expirationDate = expirationDate;
  }

  /**
   * Constructs a BillDTO object from a Bill object.
   *
   * @param bill the Bill object to construct the BillDTO from.
   */
  public BillDTO(Bill bill) {
    this.title = bill.getTitle();
    this.description = bill.getDescription();
    this.expirationDate = bill.getExpirationDate();
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public Long getId() {
    return id;
  }
}
