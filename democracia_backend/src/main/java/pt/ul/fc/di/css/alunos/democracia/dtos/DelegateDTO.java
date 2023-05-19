package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A Data Transfer Object (DTO) representing a delegate. */
public class DelegateDTO {

  private final String name;
  private final Integer cc;

  /**
   * Constructs a DelegateDTO object with the given name and ID number.
   *
   * @param name the name of the delegate.
   * @param cc the ID number of the delegate.
   */
  public DelegateDTO(String name, Integer cc) {
    this.name = name;
    this.cc = cc;
  }

  /**
   * Returns the ID number of the delegate.
   *
   * @return the ID number of the delegate.
   */
  public Integer getCc() {
    return cc;
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
