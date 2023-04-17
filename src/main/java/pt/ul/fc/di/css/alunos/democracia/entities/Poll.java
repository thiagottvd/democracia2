package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;
import pt.ul.fc.di.css.alunos.democracia.dataacess.VoteType;

@Entity
public class Poll {
  @Id @GeneratedValue private Long id;
  private int numPositiveVotes = 0;
  private int numNegativeVotes = 0;

  // HashMap isnt working
  private List<Long> publicVoters = new ArrayList<>();
  private List<VoteType> publicVotersVote = new ArrayList<>();

  private LocalDate closingDate;

  @Enumerated(EnumType.STRING)
  private PollStatus status;

  @OneToMany private List<Citizen> privateVoters = new ArrayList<>();

  @OneToOne(mappedBy = "associatedPoll")
  private Bill associatedBill;

  protected Poll() {
    // Empty constructor required by JPA.
  }

  public Poll(Bill associatedBill) {
    this.associatedBill = associatedBill;
    this.closingDate = associatedBill.getExpirationDate();
    this.status = PollStatus.ACTIVE;
    incPositiveVotes();
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

    publicVoters.add(delegate.getId());
    publicVotersVote.add(voteType);
    // privateVoters.add(delegate);

  }

  public void autoVote(List<Citizen> citizens, VoteType voteType) {
    for (Citizen c : citizens) {
      addPrivateVoter(c, voteType);
    }
  }

  public VoteType getPublicVote(Long id) {
    return publicVotersVote.get(publicVoters.indexOf(id));
  }

  public boolean hasVoted(Citizen c) {

    for (Citizen privateVoter : privateVoters) {
      if (privateVoter.getId().equals(c.getId())) {
        return true;
      }
    }

    return publicVoters.contains(c.getId());
  }

  public boolean hasExpired() {
    return closingDate.isBefore(LocalDate.now());
  }
}
