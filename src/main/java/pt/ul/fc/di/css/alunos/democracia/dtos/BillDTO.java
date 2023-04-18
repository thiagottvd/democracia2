package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

/** A Data Transfer Object (DTO) representing a bill. */
public class BillDTO {

  private Long id;
  private final String title;
  private String description;
  private LocalDate expirationDate;

  /**
   * Constructs a BillDTO object with the given ID and title.
   *
   * @param id the ID of the bill.
   * @param title the title of the bill.
   */
  public BillDTO(Long id, String title) {
    this.id = id;
    this.title = title;
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

  /**
   * Returns the title of the bill.
   *
   * @return the title of the bill.
   */
  public String getTitle() {
    return title;
  }

  /**
   * Returns the description of the bill.
   *
   * @return the description of the bill.
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the expiration date of the bill.
   *
   * @return the expiration date of the bill.
   */
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /**
   * Returns the ID of the bill.
   *
   * @return the ID of the bill.
   */
  public Long getId() {
    return id;
  }
}
