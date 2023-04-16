package pt.ul.fc.di.css.alunos.democracia.dtos;

import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;

public class DelegateDTO {

    private String name;

    private Long id;

    public DelegateDTO(String name, Long id){
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
