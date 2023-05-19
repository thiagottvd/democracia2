package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;
import java.util.List;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.springframework.lang.NonNull;

/** Represents a theme. */
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
   * Creates a new Theme object with the given designation and parent theme.
   *
   * @param designation the name or title of the theme.
   * @param parentTheme the parent theme of the theme.
   */
  public Theme(@NonNull String designation, Theme parentTheme) {
    this.designation = designation;
    this.parentTheme = parentTheme;
  }

  /**
   * Returns the unique identifier of this theme.
   *
   * @return the unique identifier of this theme
   */
  public Long getId() {
    return id;
  }

  /**
   * Returns the parent theme of this theme.
   *
   * @return the parent theme of this theme.
   */
  public Theme getParentTheme() {
    return this.parentTheme;
  }

  /**
   * Returns the designation of this theme.
   *
   * @return the designation of this theme
   */
  @NonNull
  public String getDesignation() {
    return this.designation;
  }
}
