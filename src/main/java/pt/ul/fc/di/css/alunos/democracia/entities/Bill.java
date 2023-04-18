package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenAlreadySupportsBillException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.VoteInClosedBillException;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;

  private String title;

  private String description;

  private int numSupporters = 1;

  @Lob private byte[] fileData;

  @OneToMany
  @Cascade(CascadeType.ALL)
  private List<Citizen> supporters = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private BillStatus status = BillStatus.OPEN;

  @OneToOne
  @Cascade(CascadeType.ALL)
  private Theme theme;

  @OneToOne
  @Cascade(CascadeType.ALL)
  private Delegate delegate;

  @OneToOne
  @Cascade(CascadeType.ALL)
  private Poll associatedPoll;

  protected Bill() {
    // Empty constructor required by JPA.
  }

  public Bill(
      String title,
      String description,
      byte[] fileData,
      LocalDate expirationDate,
      Delegate delegate,
      Theme theme) {
    this.title = title;
    this.description = description;
    this.fileData = fileData;
    this.expirationDate = expirationDate;
    this.delegate = delegate;
    this.theme = theme;
    supporters.add(delegate);
  }

  /*
   Returns a boolean indicating if a voter is supporting this Bill.
   @param voter The citizen to check.
  */
  public boolean supportsBill(Citizen voter) {
    return supporters.contains(voter);
  }

  /*
   Adds a new voter to the supporters list if he hasn't voted yet.
   @param voter The citizen to add.
  */
  public void addSupporterVote(Citizen voter)
      throws CitizenAlreadySupportsBillException, VoteInClosedBillException {
    if (supportsBill(voter)) {
      throw new CitizenAlreadySupportsBillException(
          "The citizen with cc "
              + voter.getCc()
              + " already supports bill with id "
              + this.getId()
              + ".");
    }
    if (this.getStatus().equals(BillStatus.CLOSED)) {
      throw new VoteInClosedBillException(
          "The citizen with cc "
              + voter.getCc()
              + " can not vote for bill with id "
              + this.getId()
              + " because it is closed.");
    }
    supporters.add(voter);
    numSupporters++;
  }

  public void setStatus(BillStatus status) {
    this.status = status;
  }

  public void setPoll(Poll associatedPoll) {
    this.associatedPoll = associatedPoll;
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
        && Arrays.equals(fileData, bill.fileData)
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
        Arrays.hashCode(fileData),
        expirationDate,
        status,
        numSupporters,
        theme,
        delegate,
        associatedPoll);
  }

  /**
   * Retrieves the bill file data.
   *
   * @return the bill file data.
   */
  public byte[] getFileData() {
    return fileData;
  }
}
