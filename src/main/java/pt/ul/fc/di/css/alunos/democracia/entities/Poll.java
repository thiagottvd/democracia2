package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.time.LocalDate;
import pt.ul.fc.di.css.alunos.democracia.dataacess.PollStatus;

@Entity
public class Poll {
  @Id @GeneratedValue private Long id;
  private int numPositiveVotes = 0;
  private int numNegativeVotes = 0;
  private LocalDate closingDate;

  @Enumerated(EnumType.STRING)
  private PollStatus status = PollStatus.ACTIVE;

  @OneToOne(mappedBy = "associatedPoll")
  private Bill associatedBill;

  protected Poll() {
    // Empty constructor required by JPA.
  }

  public Poll(Bill associatedBill) {
    this.associatedBill = associatedBill;
    this.closingDate = associatedBill.getExpirationDate();
  }

  public Long getId() {
    return id;
  }

  public Bill getBill() {
    return associatedBill;
  }

  public void setStatus(PollStatus status) {
    this.status = status;
  }
}
