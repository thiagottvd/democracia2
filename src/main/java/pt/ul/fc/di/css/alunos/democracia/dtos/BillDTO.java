package pt.ul.fc.di.css.alunos.democracia.dtos;

import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;

public class BillDTO {

  private Long id;
  private String title;
  private String description;
  private int numSupporters;
  // TODO private byte[] fileData;
  private LocalDate expirationDate;
  private String theme;
  private String delegate;

  public BillDTO(Long id, String title) {
    this.id = id;
    this.title = title;
  }

  public BillDTO(Bill bill) {
    this.id = bill.getId();
    this.title = bill.getTitle();
    this.description = bill.getDescription();
    this.numSupporters = bill.getNumSupporters();
    // TODO this.fileData = bill.getFileData();
    this.expirationDate = bill.getExpirationDate();
    this.theme = bill.getTheme().getDesignation();
    this.delegate = bill.getDelegate().getName();
  }

  public Long getId() {
    return id;
  }
}
