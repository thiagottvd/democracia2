package pt.ul.fc.di.css.alunos.democracia.services;

import org.springframework.stereotype.Service;
import pt.ul.fc.di.css.alunos.democracia.handlers.SupportBillHandler;

@Service
public class SupportBillService {

  private final SupportBillHandler supportBillHandler;

  public SupportBillService(SupportBillHandler supportBillHandler) {
    this.supportBillHandler = supportBillHandler;
  }

  public void supportBill(Long billId, int nif) {
    supportBillHandler.supportBill(billId, nif);
  }
}
