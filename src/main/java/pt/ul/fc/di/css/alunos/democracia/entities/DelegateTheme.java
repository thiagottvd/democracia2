package pt.ul.fc.di.css.alunos.democracia.entities;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class DelegateTheme {

    @Id @GeneratedValue
    private Long id;

    @OneToOne
    private Theme theme;

    @OneToOne
    private Delegate delegate;

    @ManyToMany
    private List<Citizen> voters;

    protected DelegateTheme(){
        // Empty constructor required by JPA.
    }

    public DelegateTheme(Theme theme, Delegate delegate){
        this.theme = theme;
        this.delegate = delegate;
        voters = new ArrayList<Citizen>();
    }

    public Delegate getDelegate() {
        return delegate;
    }

    public Theme getTheme() {
        return theme;
    }

    public void addVoter(Citizen citizen){

        voters.add(citizen);

    }

    public List<Citizen> getVoters() {
        return voters;
    }

    public boolean checkTheme(Theme t){
        return theme.getDesignation().equals(t.getDesignation());
    }

    /*
        Checks if the Delegate and Theme given are the same as the attributes
     */
    public boolean checkDelegateTheme(Delegate d, Theme t){
        return d.getId().equals(delegate.getId()) && t.getDesignation().equals(theme.getDesignation());
    }
}