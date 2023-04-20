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
import org.springframework.lang.NonNull;
import pt.ul.fc.di.css.alunos.democracia.datatypes.BillStatus;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenAlreadySupportsBillException;
import pt.ul.fc.di.css.alunos.democracia.exceptions.VoteInClosedBillException;

/** Represents a Bill. */
@Entity
public class Bill {

  @Id @GeneratedValue private Long id;

  @NonNull private String title;

  @NonNull private String description;

  private int numSupporters = 1;

  @Lob @NonNull private byte[] fileData;

  @ManyToMany
  @Cascade(CascadeType.ALL)
  private final List<Citizen> supporters = new ArrayList<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NonNull
  private LocalDate expirationDate;

  @Enumerated(EnumType.STRING)
  private BillStatus status = BillStatus.OPEN;

  @OneToOne
  @Cascade(CascadeType.ALL)
  @NonNull
  private Theme theme;

  @OneToOne
  @Cascade(CascadeType.ALL)
  @NonNull
  private Delegate delegate;

  @OneToOne(mappedBy = "associatedBill")
  @Cascade(CascadeType.ALL)
  private Poll associatedPoll;

  protected Bill() {
    // Empty constructor required by JPA.
  }

  /**
   * Represents a bill proposal and its relevant information, including the title, description, pdf
   * file data, expiration date, delegate who proposed it, theme it is related to, and a list of
   * supporters.
   *
   * @param title the title of the bill.
   * @param description a description of the bill.
   * @param fileData the pdf file data of the bill.
   * @param expirationDate the date the bill will expire.
   * @param delegate the delegate who proposed the bill.
   * @param theme the theme the bill is related to.
   */
  public Bill(
      @NonNull String title,
      @NonNull String description,
      @NonNull byte[] fileData,
      @NonNull LocalDate expirationDate,
      @NonNull Delegate delegate,
      @NonNull Theme theme) {
    this.title = title;
    this.description = description;
    this.fileData = fileData;
    this.expirationDate = expirationDate;
    this.delegate = delegate;
    this.theme = theme;
    supporters.add(delegate);
  }

  /**
   * Checks if the given citizen supports this bill.
   *
   * @param voter the citizen to check if they support the bill.
   * @return true if the citizen supports the bill; false otherwise.
   */
  public boolean supportsBill(Citizen voter) {
    return supporters.contains(voter);
  }

  /**
   * Adds a supporter vote to the bill if the voter has not voted yet and if the bill is not closed.
   *
   * @param voter the citizen who is voting for the bill.
   * @throws CitizenAlreadySupportsBillException if the citizen already supports the bill.
   * @throws VoteInClosedBillException if the bill is already closed and no more votes are allowed.
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

  /**
   * Sets the status of the bill.
   *
   * @param status The status of the bill to be set.
   */
  public void setStatus(BillStatus status) {
    this.status = status;
  }

  /**
   * Sets the poll associated with the bill.
   *
   * @param associatedPoll The poll associated with the bill.
   */
  public void setPoll(Poll associatedPoll) {
    this.associatedPoll = associatedPoll;
  }

  /**
   * Gets the ID of the bill.
   *
   * @return The ID of the bill.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the title of the bill.
   *
   * @return the title of the bill
   */
  @NonNull
  public String getTitle() {
    return title;
  }

  /**
   * Returns the status of the bill.
   *
   * @return the status of the bill
   */
  public BillStatus getStatus() {
    return status;
  }

  /**
   * Returns the expiration date of the bill.
   *
   * @return the expiration date of the bill
   */
  @NonNull
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /**
   * Returns the number of citizens who support this bill.
   *
   * @return the number of supporters of this bill
   */
  public int getNumSupporters() {
    return supporters.size();
  }

  /**
   * Returns the theme of this bill.
   *
   * @return the theme of this bill.
   */
  @NonNull
  public Theme getTheme() {
    return theme;
  }

  /**
   * Returns the bill description.
   *
   * @return the bill description.
   */
  @NonNull
  public String getDescription() {
    return description;
  }

  /**
   * Returns the delegate associated with this bill.
   *
   * @return The delegate associated with this bill.
   */
  @NonNull
  public Delegate getDelegate() {
    return delegate;
  }

  /**
   * Returns the poll associated with this bill, if any.
   *
   * @return The poll associated with this bill, or null if there is no poll associated.
   */
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
