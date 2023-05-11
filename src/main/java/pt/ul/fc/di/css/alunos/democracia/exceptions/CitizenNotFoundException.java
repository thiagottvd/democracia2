package pt.ul.fc.di.css.alunos.democracia.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * The CitizenNotFoundException is an exception that is thrown when a citizen (includes delegate)
 * cannot be found. It is a subclass of ApplicationException, which is itself a subclass of
 * Exception.
 */
@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CitizenNotFoundException extends ApplicationException {

  /**
   * Constructs a new CitizenNotFoundException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public CitizenNotFoundException(String message) {
    super(message);
  }
}
