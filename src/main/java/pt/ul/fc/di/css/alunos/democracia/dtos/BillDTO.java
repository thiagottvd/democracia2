package pt.ul.fc.di.css.alunos.democracia.dtos;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

/** A Data Transfer Object (DTO) representing a bill. */
public class BillDTO {

  private Long id;

  @NotNull
  @Size(min = 3, max = 125)
  private String title;

  @Size(min = 3, max = 250)
  private String description;

  private int numSupporters;
  private byte[] fileData;
  @NotNull private LocalDate expirationDate;
  @NotNull private String themeDesignation;
  private String delegateName;

  /** Constructs an empty BillDTO object. */
  public BillDTO() {}

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
    this.themeDesignation = bill.getTheme().getDesignation();
    this.delegateName = bill.getDelegate().getName();
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

  /**
   * Returns the number of supporters of the bill.
   *
   * @return the number of supporters of the bill.
   */
  public int getNumSupporters() {
    return numSupporters;
  }

  /**
   * Returns the byte array of the bill file data.
   *
   * @return the byte array of the bill file data.
   */
  public byte[] getFileData() {
    return fileData;
  }

  /**
   * Returns the theme designation of the bill.
   *
   * @return the theme designation of the bill.
   */
  public String getThemeDesignation() {
    return themeDesignation;
  }

  /**
   * Returns the name of the delegate who proposed the bill.
   *
   * @return the name of the delegate who proposed the bill.
   */
  public String getDelegateName() {
    return delegateName;
  }

  /**
   * Sets the ID of the bill.
   *
   * @param id the ID of the bill.
   */
  public void setId(Long id) {
    this.id = id;
  }

  /**
   * Sets the title of the bill.
   *
   * @param title the title of the bill.
   */
  public void setTitle(String title) {
    this.title = title;
  }

  /**
   * Sets the description of the bill.
   *
   * @param description the description of the bill.
   */
  public void setDescription(String description) {
    this.description = description;
  }

  /**
   * Sets the byte array of the bill file data.
   *
   * @param fileData the byte array of the bill file data to set.
   */
  public void setFileData(byte[] fileData) {
    this.fileData = fileData;
  }

  /**
   * Sets the expiration date of the bill.
   *
   * @param expirationDate the expiration date of the bill.
   */
  public void setExpirationDate(LocalDate expirationDate) {
    this.expirationDate = expirationDate;
  }

  /**
   * Sets the theme designation of the bill.
   *
   * @param themeDesignation the theme designation of the bill.
   */
  public void setThemeDesignation(String themeDesignation) {
    this.themeDesignation = themeDesignation;
  }

  /**
   * Sets the delegate name who is responsible for proposing the bill.
   *
   * @param delegateName the name of the delegate who proposed the bill.
   */
  public void setDelegateName(String delegateName) {
    this.delegateName = delegateName;
  }
}
