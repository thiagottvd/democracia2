package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;

import java.util.List;

@Entity
public class Theme {
    @Id
    @GeneratedValue
    private Long id;
    private String designation;

    @OneToMany
    private List<Theme> subThemes;
    @OneToOne
    private Theme parentTheme;

    public Theme(){
        // Empty constructor required by JPA.
    }

    /*
        Theme class constructor.
        @param designation The theme designation.
        @param parentTheme This theme parentTheme.
     */
    public Theme(String designation, Theme parentTheme){
        this.designation = designation;
        this.parentTheme = parentTheme;
    }

    public Long getId() {
        return id;
    }

    /*
        Returns the parent theme.
     */
    public Theme getParentTheme(){
        return this.parentTheme;
    }

    /*
        Returns the theme designation.
     */
    public String getDesignation(){
        return this.designation;
    }

    /*
        Sets a new parent theme.
        @param parentTheme The new parent theme.
     */
    public void setParentTheme(Theme parentTheme){
        this.parentTheme = parentTheme;
    }
}
