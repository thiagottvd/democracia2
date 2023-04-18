package pt.ul.fc.di.css.alunos.democracia.dtos;

/*
 * A data transfer object (DTO) representing a theme.
 */
public class ThemeDTO {
  private final String designation;

  /**
   * Constructs a ThemeDTO object with the given designation.
   *
   * @param designation the designation of the theme.
   */
  public ThemeDTO(String designation) {
    this.designation = designation;
  }

  /**
   * Returns the designation of the theme.
   *
   * @return the designation of the theme.
   */
  public String getDesignation() {
    return designation;
  }
}
