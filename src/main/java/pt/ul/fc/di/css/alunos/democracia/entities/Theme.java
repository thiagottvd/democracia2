package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;

@Entity
public class Theme {
  @Id @GeneratedValue private Long id;
  @NonNull private String designation;

  @OneToMany(mappedBy = "parentTheme")
  @Cascade(CascadeType.ALL)
  private List<Theme> subThemes;

  @ManyToOne private Theme parentTheme;

  public Theme() {
    // Empty constructor required by JPA.
  }

  /**
   * Theme class constructor.
   * @param designation The theme designation.
   * @param parentTheme The theme parent theme.
   */
  public Theme(String designation, Theme parentTheme) {
    this.designation = designation;
    this.parentTheme = parentTheme;
  }

  /***** GETTERS *****/

  public Long getId() {
    return id;
  }

  public Theme getParentTheme() {
    return this.parentTheme;
  }

  public String getDesignation() {
    return this.designation;
  }

  /***** SETTERS *****/

  public void setParentTheme(Theme parentTheme) {
    this.parentTheme = parentTheme;
  }
}
