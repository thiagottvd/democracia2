package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The InvalidVoteTypeException is an exception that is thrown when a vote type is invalid or null.
 * It is a subclass of ApplicationException, which is itself a subclass of Exception.
 */
public class InvalidVoteTypeException extends ApplicationException {

  /**
   * Constructs a new InvalidVoteTypeException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public InvalidVoteTypeException(String message) {
    super(message);
  }
}
