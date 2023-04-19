package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The CitizenAlreadyVotedException is an exception that is thrown when a citizen is trying to vote
 * in a poll that he already voted. It is a subclass of ApplicationException, which is itself a
 * subclass of Exception.
 */
public class CitizenAlreadyVotedException extends ApplicationException {

  /**
   * Constructs a new CitizenAlreadyVotedException with the specified error message.
   *
   * @param message the error message to be associated with the exception.
   */
  public CitizenAlreadyVotedException(String message) {
    super(message);
  }

  /**
   * Constructs a new CitizenAlreadyVotedException with the specified error message and a reference
   * to the lower-level exception that caused this exception.
   *
   * @param message the error message to be associated with the exception.
   * @param e the lower-level exception that caused this exception to be thrown.
   */
  public CitizenAlreadyVotedException(String message, Exception e) {
    super(message, e);
  }
}
