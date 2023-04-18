package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dataacess.VoteType;

@Entity
public class Poll {

  @Id @GeneratedValue private Long id;

  private int numPositiveVotes = 0;

  private int numNegativeVotes = 0;

  @ElementCollection // Indicates that publicVoters is a collection of simple or embeddable types.
  @CollectionTable(
      name = "public_voters") // Specifies the name of the table that will hold the collection
  // elements. In this case, the table name is "public_voters".
  @MapKeyJoinColumn(
      name = "delegate_id") // Specifies the name of the foreign key column that references the
  // Delegate entity. The column name is "delegate_id".
  @Column(
      name =
          "vote_type") // Specifies the name of the column that holds the VoteType enum value. The
  // column name is "vote_type".
  @Enumerated(
      EnumType
          .STRING) // Indicates that the VoteType enum should be stored as a string in the database.
  private Map<Delegate, VoteType> publicVoters = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NonNull
  private LocalDate closingDate;

  @Enumerated(EnumType.STRING)
  private PollStatus status;

  @OneToMany private List<Citizen> privateVoters = new ArrayList<>();

  @OneToOne(mappedBy = "associatedPoll")
  @Cascade(CascadeType.ALL)
  @NonNull
  private Bill associatedBill;

  protected Poll() {
    // Empty constructor required by JPA.
  }

  public Poll(Bill associatedBill) {
    this.associatedBill = associatedBill;
    this.closingDate = associatedBill.getExpirationDate();
    this.status = PollStatus.ACTIVE;
    addPublicVoter(this.associatedBill.getDelegate(), VoteType.POSITIVE);
  }

  public Long getId() {
    return id;
  }

  public Bill getBill() {
    return associatedBill;
  }

  public List<Citizen> getPrivateVoters() {
    return privateVoters;
  }

  public PollStatus getStatus() {
    return status;
  }

  @NonNull
  public Bill getAssociatedBill() {
    return associatedBill;
  }

  public int getNumNegativeVotes() {
    return numNegativeVotes;
  }

  public int getNumPositiveVotes() {
    return numPositiveVotes;
  }

  public void incPositiveVotes() {
    numPositiveVotes++;
  }

  public void incNegativeVotes() {
    numNegativeVotes++;
  }

  public void setStatus(PollStatus status) {
    this.status = status;
  }

  public void addPrivateVoter(Citizen citizen, VoteType voteType) {

    if (voteType == VoteType.POSITIVE) {
      incPositiveVotes();
    } else {
      incNegativeVotes();
    }

    privateVoters.add(citizen);
  }

  public void addPublicVoter(Delegate delegate, VoteType voteType) {

    if (voteType == VoteType.POSITIVE) {
      incPositiveVotes();
    } else {
      incNegativeVotes();
    }
    publicVoters.put(delegate, voteType);
  }

  public VoteType getPublicVote(Delegate delegate) {
    return publicVoters.get(delegate);
  }

  public LocalDate getClosingDate() {
    return closingDate;
  }

  public Map<Delegate, VoteType> getPublicVoters() {
    return publicVoters;
  }

  public List<Citizen> getAllVoters() {
    ArrayList<Citizen> voters = new ArrayList<>();
    voters.addAll(this.getPrivateVoters());
    voters.addAll(this.getPublicVoters().keySet());
    return voters;
  }
}
