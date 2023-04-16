package pt.ul.fc.di.css.alunos.democracia.services;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.dtos.BillDTO;
import pt.ul.fc.di.css.alunos.democracia.exceptions.ApplicationException;
import pt.ul.fc.di.css.alunos.democracia.handlers.ConsultBillsHandler;

@Service
public class ConsultBillsService {

  private final ConsultBillsHandler consultBillsHandler;

  @Autowired
  public ConsultBillsService(ConsultBillsHandler consultBIllsHandler) {
    this.consultBillsHandler = consultBIllsHandler;
  }

  public List<BillDTO> getOpenBills() {
    return consultBillsHandler.getOpenBills();
  }

  public BillDTO getBillDetails(Long billId) throws ApplicationException {
    return consultBillsHandler.getBillDetails(billId);
  }
}
