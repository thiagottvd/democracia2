package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;
  private String title;
  private String description;
  private File file;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private BillStatus status = BillStatus.OPEN;

  private int numSupporters;
  @OneToOne private Theme theme;
  @OneToOne private Delegate delegate;
  @OneToOne private Poll associatedPoll;

  protected Bill() {
    // Empty constructor required by JPA.
  }

  public Bill(
      String title,
      String description,
      File file,
      LocalDate expirationDate,
      Delegate delegate,
      Theme theme) {
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

  public BillStatus getStatus() {
    return status;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    Bill bill = (Bill) o;
    return numSupporters == bill.numSupporters
        && Objects.equals(id, bill.id)
        && Objects.equals(title, bill.title)
        && Objects.equals(description, bill.description)
        && Objects.equals(file, bill.file)
        && Objects.equals(expirationDate, bill.expirationDate)
        && status == bill.status
        && Objects.equals(theme, bill.theme)
        && Objects.equals(delegate, bill.delegate)
        && Objects.equals(associatedPoll, bill.associatedPoll);
  }

  @Override
  public int hashCode() {
    return Objects.hash(
        id,
        title,
        description,
        file,
        expirationDate,
        status,
        numSupporters,
        theme,
        delegate,
        associatedPoll);
  }
}
