package pt.ul.fc.di.css.alunos.democracia.dtos;

public class ThemeDTO {
    private String designation;

    /*
        ThemeDTO class constructor.
        @param designation The theme designation.
     */
    public ThemeDTO(String designation){ this.designation = designation; }

    public String getDesignation(){
        return designation;
    }
}
