package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.springframework.format.annotation.DateTimeFormat;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;
  private String title;
  private String description;
  private int numSupporters = 0;
  private File file;
  @OneToMany private List<Citizen> supporters = new ArrayList<>();;

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private BillStatus status = BillStatus.OPEN;

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

  /*
   Returns a boolean indicating if a voter is supporting this Bill.
   @param voter The citizen to check.
  */
  public boolean checkSupport(Citizen voter) {
    for (Citizen c : supporters) {
      if (voter.equals(c)) return true;
    }
    return false;
  }

  /*
   Adds a new voter to the supporters list if he hasn't voted yet.
   @param voter The citizen to add.
  */
  public void addSupporter(Citizen voter) {
    if (!checkSupport(voter)) {
      supporters.add(voter);
    }
  }

  /*
   Sets the Bill status to closed.
  */
  public void setClosedStatus() {
    status = BillStatus.CLOSED;
  }

  public Long getId() {
    return id;
  }

  /*
   Returns the Bill title.
  */
  public String getTitle() {
    return title;
  }

  /*
   Returns the Bill expiration date.
  */
  public BillStatus getStatus() {
    return status;
  }

  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /*
   Returns the number of supporters in this Bill.
  */
  public int getNumSupporters() {
    return supporters.size();
  }

  /*
   Returns the Bill theme.
  */
  public Theme getTheme() {
    return theme;
  }

  /**
   * Returns the bill description.
   *
   * @return the bill description.
   */
  public String getDescription() {
    return description;
  }

  public Delegate getDelegate() {
    return delegate;
  }

  public Poll getPoll() {
    return associatedPoll;
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
