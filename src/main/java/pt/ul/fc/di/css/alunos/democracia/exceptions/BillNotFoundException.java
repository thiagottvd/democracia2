package pt.ul.fc.di.css.alunos.democracia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The BillNotFoundException is an exception that is thrown when a bill cannot be found. It is a
 * subclass of ApplicationException, which is itself a subclass of Exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class BillNotFoundException extends ApplicationException {

  /**
   * Constructs a new BillNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public BillNotFoundException(String message) {
    super(message);
  }
}
