package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Citizen;
import pt.ul.fc.di.css.alunos.democracia.entities.Delegate;
import pt.ul.fc.di.css.alunos.democracia.repositories.CitizenRepository;

import java.util.List;

@Component
public class CitizenCatalog {
    private final CitizenRepository citizenRepository;

    @Autowired
    public CitizenCatalog(CitizenRepository citizenRepository){ this.citizenRepository = citizenRepository; }

    public Citizen getCitizen(Citizen c){
        //TODO verifications
        return citizenRepository.findCitizenByNif(c.getNif());
    }

    public Delegate getDelegate(int nif){
        //TODO verifications e perceber se Delegates vao ter tabela propria ou nao
        Citizen c = citizenRepository.findCitizenByNif(nif);
        return new Delegate(c.getName(), c.getNif());
    }

    public List<Delegate> getDelegates(){
        //TODO same as above
        return null;
    }
}
