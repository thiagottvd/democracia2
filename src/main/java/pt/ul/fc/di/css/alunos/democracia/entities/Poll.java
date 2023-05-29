package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.*;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.lang.NonNull;
import pt.ul.fc.di.css.alunos.democracia.datatypes.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.datatypes.VoteType;
import pt.ul.fc.di.css.alunos.democracia.exceptions.CitizenAlreadyVotedException;

/** Class that represents a poll. */
@Entity
public class Poll {

  @Id @GeneratedValue private Long id;

  private int numPositiveVotes = 0;

  private int numNegativeVotes = 0;

  @ElementCollection(fetch = FetchType.EAGER)
  @CollectionTable(name = "public_voters")
  @MapKeyJoinColumn(name = "delegate_id")
  @Column(name = "vote_type")
  @Enumerated(EnumType.STRING)
  @Cascade(CascadeType.ALL)
  private final Map<Delegate, VoteType> publicVoters = new HashMap<>();

  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
  @NonNull
  private LocalDate closingDate;

  @Enumerated(EnumType.STRING)
  private PollStatus status;

  @ManyToMany(fetch = FetchType.EAGER)
  private final List<Citizen> privateVoters = new ArrayList<>();

  @OneToOne
  @Cascade(CascadeType.ALL)
  @NonNull
  private Bill associatedBill;

  protected Poll() {
    // Empty constructor required by JPA.
  }

  /**
   * Constructs a Poll object with the given Bill object.
   *
   * @param associatedBill the bill associated with this poll.
   */
  public Poll(Bill associatedBill) {
    this.associatedBill = associatedBill;
    this.closingDate = associatedBill.getExpirationDate();
    this.status = PollStatus.ACTIVE;
    addPublicVoter(this.associatedBill.getDelegate(), VoteType.POSITIVE);
    associatedBill.setPoll(this);
  }

  /**
   * Returns the ID of this poll.
   *
   * @return the ID of this poll.
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the list of private voters in this poll.
   *
   * <p>This method is public for testing purposes.
   *
   * @return the list of private voters in this poll.
   */
  public List<Citizen> getPrivateVoters() {
    return privateVoters;
  }

  /**
   * Returns the current status of the poll.
   *
   * @return the current status of the poll.
   */
  public PollStatus getStatus() {
    return status;
  }

  /**
   * Returns the bill associated with this poll.
   *
   * @return the bill associated with this poll.
   */
  @NonNull
  public Bill getAssociatedBill() {
    return associatedBill;
  }

  /**
   * Returns the number of negative votes in this poll.
   *
   * @return the number of negative votes in this poll.
   */
  public int getNumNegativeVotes() {
    return numNegativeVotes;
  }

  /**
   * Returns the number of positive votes in this poll.
   *
   * @return the number of positive votes in this poll.
   */
  public int getNumPositiveVotes() {
    return numPositiveVotes;
  }

  /** Increases the number of positive votes in this poll by 1. */
  public void incPositiveVotes() {
    numPositiveVotes++;
  }

  /** Increments the number of negative votes in the poll by 1. */
  public void incNegativeVotes() {
    numNegativeVotes++;
  }

  /**
   * Sets the status of the poll to the given status.
   *
   * @param status the new status of the poll
   */
  public void setStatus(PollStatus status) {
    this.status = status;
  }

  /**
   * Adds a voter to the poll, either as a public or private voter, depending on the citizen's type.
   *
   * @param citizen the citizen who wants to vote.
   * @param option the citizen's vote option.
   * @throws CitizenAlreadyVotedException if the citizen has already voted in this poll.
   */
  public void addVoter(Citizen citizen, VoteType option) throws CitizenAlreadyVotedException {
    if (hasVoted(citizen)) {
      throw new CitizenAlreadyVotedException(
          "Citizen with citizen card number "
              + citizen.getCitizenCardNumber()
              + " has already voted in this poll.");
    }
    if (citizen.getClass() == Delegate.class) {
      addPublicVoter((Delegate) citizen, option);
    } else {
      addPrivateVoter(citizen, option);
    }
  }

  /**
   * Adds a private voter to the poll and increments the respective vote count.
   *
   * <p>This method is public for testing purposes.
   *
   * @param citizen the citizen who wants to vote privately.
   * @param voteType the citizen's vote option.
   */
  public void addPrivateVoter(Citizen citizen, VoteType voteType) {
    if (voteType == VoteType.POSITIVE) {
      incPositiveVotes();
    } else {
      incNegativeVotes();
    }
    privateVoters.add(citizen);
  }

  /**
   * Adds a public voter to the poll and increments the respective vote count.
   *
   * <p>This method is public for testing purposes.
   *
   * @param delegate the delegate who wants to vote publicly.
   * @param voteType the delegate's vote option.
   */
  public void addPublicVoter(Delegate delegate, VoteType voteType) {
    if (voteType == VoteType.POSITIVE) {
      incPositiveVotes();
    } else {
      incNegativeVotes();
    }
    publicVoters.put(delegate, voteType);
  }

  /**
   * Checks if a given citizen has already voted in this poll.
   *
   * @param citizen the citizen to check.
   * @return true if the citizen has already voted, false otherwise.
   */
  private boolean hasVoted(Citizen citizen) {
    return privateVoters.contains(citizen) || publicVoters.containsKey(citizen);
  }

  /**
   * Returns the vote option of the given delegate in this poll.
   *
   * @param delegate the delegate whose vote option should be returned.
   * @return the vote option of the given delegate.
   */
  public VoteType getDelegateVote(Delegate delegate) {
    return publicVoters.get(delegate);
  }

  /**
   * Returns the closing date of this poll.
   *
   * <p>This method is public for testing purposes.
   *
   * @return the closing date of this poll.
   */
  @NonNull
  public LocalDate getClosingDate() {
    return closingDate;
  }

  /**
   * Returns the map of public voters in this poll.
   *
   * @return the map of public voters in this poll.
   */
  public Map<Delegate, VoteType> getPublicVoters() {
    return publicVoters;
  }

  /**
   * Returns a list with all the voters in this poll, both private and public.
   *
   * @return a list with all the voters in this poll.
   */
  public List<Citizen> getAllVoters() {
    ArrayList<Citizen> voters = new ArrayList<>();
    voters.addAll(this.getPrivateVoters());
    voters.addAll(this.getPublicVoters().keySet());
    return voters;
  }
}
