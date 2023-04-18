package pt.ul.fc.di.css.alunos.democracia.dtos;

/** A Data Transfer Object (DTO) representing a Theme. */
public class ThemeDTO {
  private final String designation;


  /**
   * Constructs a ThemeDTO object from a Theme object.
   * @param designation The theme designation.
   */
  public ThemeDTO(String designation) {
    this.designation = designation;
  }

  /***** GETTERS *****/

  public String getDesignation() {
    return designation;
  }
}
