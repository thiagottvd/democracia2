package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A Data Transfer Object (DTO) representing a delegate. */
public class DelegateDTO {

  private final String name;
  private final Integer citizenCardNumber;

  /**
   * Constructs a DelegateDTO object with the given name and his citizen card number.
   *
   * @param name the name of the delegate.
   * @param citizenCardNumber the citizen card number of the delegate.
   */
  public DelegateDTO(String name, Integer citizenCardNumber) {
    this.name = name;
    this.citizenCardNumber = citizenCardNumber;
  }

  /**
   * Returns the citizen card number of the delegate.
   *
   * @return the citizen card number of the delegate.
   */
  public Integer getCitizenCardNumber() {
    return citizenCardNumber;
  }

  /**
   * Returns the name of the delegate.
   *
   * @return the name of the delegate.
   */
  public String getName() {
    return name;
  }
}
