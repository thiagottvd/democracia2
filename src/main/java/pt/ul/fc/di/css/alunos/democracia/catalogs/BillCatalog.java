package pt.ul.fc.di.css.alunos.democracia.catalogs;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pt.ul.fc.di.css.alunos.democracia.entities.Bill;
import pt.ul.fc.di.css.alunos.democracia.repositories.BillRepository;

import java.util.List;

@Component
public class BillCatalog {
    private final BillRepository billRepository;

    @Autowired
    public BillCatalog(BillRepository billRepository){ this.billRepository = billRepository; }

    public List<Bill> getOpenBills(){
        return billRepository.findAllOpenBills();
    }
}
