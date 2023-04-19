package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.Entity;

/** Represents a delegate. */
@Entity
public class Delegate extends Citizen {

  protected Delegate() {
    // Empty constructor required by JPA.
  }

  /**
   * Creates a new Delegate with the given name citizen card number.
   *
   * @param name the name of the delegate.
   * @param cc the citizen card number of the delegate.
   */
  public Delegate(String name, Integer cc) {
    super(name, cc);
  }
}
