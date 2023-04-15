package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;

import java.io.File;
import java.time.LocalDate;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;
  private String title;
  private String description;
  private File file;
  private LocalDate expirationDate;
  private BillStatus status;
  private int numSupporters;

  @OneToOne
  private Theme theme;
  @OneToOne
  private Delegate delegate;
  @OneToOne private Poll associatedPoll;

  protected Bill() {
    // Empty constructor required by JPA.
  }

  public Bill(String title, String description, File file, LocalDate expirationDate, Delegate delegate, Theme theme) {
    this.title = title;
    this.description = description;
    this.file = file;
    this.expirationDate = expirationDate;
    this.delegate = delegate;
    this.theme = theme;
  }

  public Long getId() {
    return id;
  }

  public String getTitle() {
    return title;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }
}
