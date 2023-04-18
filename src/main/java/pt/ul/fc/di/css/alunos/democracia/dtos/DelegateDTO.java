package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A Data Transfer Object (DTO) representing a Delegate. */
public class DelegateDTO {

  private final String name;
  private final int cc;

  /**
   * Constructs a DelegateDTO object from a Delegate object.
   *
   * @param name the delegate name.
   * @param cc The delegate cc.
   */
  public DelegateDTO(String name, int cc) {
    this.name = name;
    this.cc = cc;
  }

  /***** GETTERS *****/
  public int getCc() {
    return cc;
  }

  public String getName() {
    return name;
  }
}
