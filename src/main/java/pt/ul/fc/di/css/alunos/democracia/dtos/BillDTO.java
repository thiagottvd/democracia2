package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

/** A Data Transfer Object (DTO) representing a bill. */
public class BillDTO {

  private final Long id;
  private final String title;
  private String description;
  private int numSupporters;
  private byte[] fileData;
  private LocalDate expirationDate;
  private String theme;
  private String delegate;

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
    this.id = bill.getId();
    this.title = bill.getTitle();
    this.description = bill.getDescription();
    this.numSupporters = bill.getNumSupporters();
    this.fileData = bill.getFileData();
    this.expirationDate = bill.getExpirationDate();
    this.theme = bill.getTheme().getDesignation();
    this.delegate = bill.getDelegate().getName();
  }

  /**
   * Returns the ID of the bill.
   *
   * @return the ID of the bill.
   */
  public Long getId() {
    return id;
  }

  public byte[] getFileData() {
    return fileData;
  }

  public String getTitle() {
    return title;
  }

  public String getDescription() {
    return description;
  }

  public int getNumSupporters() {
    return numSupporters;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  public String getTheme() {
    return theme;
  }

  public String getDelegate() {
    return delegate;
  }
}
