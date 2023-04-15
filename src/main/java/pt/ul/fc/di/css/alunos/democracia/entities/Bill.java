package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import pt.ul.fc.di.css.alunos.democracia.dataacess.BillStatus;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Bill {

  @Id @GeneratedValue private Long id;
  private String title;
  private String description;
  private File file;
  private LocalDate expirationDate;
  private BillStatus status;
  private int numSupporters;

  // Object Associations
  @OneToOne
  private Theme theme;
  @OneToOne
  private Delegate delegate;
  @OneToOne
  private Poll associatedPoll;
  @OneToMany
  private List<Citizen> supporters;

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
    supporters = new ArrayList<>();
  }

  /*
    Returns a boolean indicating if a voter is supporting this Bill.
    @param voter The citizen to check.
   */
  public boolean checkSupport(Citizen voter){
    for(Citizen c : supporters){
      if(voter.equals(c)) return true;
    }
    return false;
  }

  /*
    Adds a new voter to the supporters list if he hasn't voted yet.
    @param voter The citizen to add.
   */
  public void addSupporter(Citizen voter){
    if(!checkSupport(voter)) {
      supporters.add(voter);
    }
  }

  /*
    Sets the Bill status to closed.
   */
  public void setClosedStatus(){
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
  public LocalDate getExpirationDate() {
    return expirationDate;
  }

  /*
    Returns the number of supporters in this Bill.
   */
  public int getNumSupporters(){
    return supporters.size();
  }

  /*
    Returns the Bill theme.
   */
  public Theme getTheme(){
    return theme;
  }
}
