package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/** Represents a delegate. */
@Entity
@DiscriminatorValue("delegate")
public class Delegate extends Citizen {

  protected Delegate() {
    // Empty constructor required by JPA.
  }

  /**
   * Creates a new Delegate with the given name citizen card number.
   *
   * @param name the name of the delegate.
   * @param citizenCardNumber the citizen card number of the delegate.
   */
  public Delegate(String name, Integer citizenCardNumber) {
    super(name, citizenCardNumber);
  }
}
