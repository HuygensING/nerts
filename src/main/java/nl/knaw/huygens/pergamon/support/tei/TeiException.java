package nl.knaw.huygens.pergamon.support.tei;

/**
 * Thrown when an error occurs while processing a TEI document.
 */
public class TeiException extends Exception {

  private static final long serialVersionUID = 1L;

  public TeiException() {}

  public TeiException(String message) {
    super(message);
  }

  public TeiException(String format, Object... args) {
    super(String.format(format, args));
  }

}
