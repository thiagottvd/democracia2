package pt.ul.fc.di.css.alunos.democracia.exceptions;

/**
 * The DelegateThemeAlreadyExistsException is an exception that is thrown when a
 * citizen tries to associate another delegate to a theme that already has a delegate
 * associated with. It is a subclass of ApplicationException, which is itself a subclass
 * of Exception.
 */
public class DuplicateDelegateThemeException extends ApplicationException {

    /**
     * Constructs a new DelegateThemeAlreadyExistsException with the specified error message.
     *
     * @param message the error message to be associated with the exception.
     */
    public DuplicateDelegateThemeException(String message) {
        super(message);
    }
}
