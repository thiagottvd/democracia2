package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;

@Service
public class ConsultBillsService {

  private final ConsultBillsHandler consultBIllsHandler;

  @Autowired
  public ConsultBillsService(ConsultBillsHandler consultBIllsHandler) {
    this.consultBIllsHandler = consultBIllsHandler;
  }

  public List<BillDTO> getOpenBills() {
    return consultBIllsHandler.getOpenBills();
  }

  public BillDTO getBillDetails(BillDTO billDTO) {
    return consultBIllsHandler.getBillDetails(billDTO);
  }
}
