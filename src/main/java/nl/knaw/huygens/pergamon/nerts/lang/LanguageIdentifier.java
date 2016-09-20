package nl.knaw.huygens.pergamon.nerts.lang;

import java.util.function.Function;

/**
 * A basic interface for encapsulating language identification.
 */
public interface LanguageIdentifier extends Function<String, String> {

  /** Language code for text that cannot be identified. */
  static String UNKNOWN_LANGUAGE = "?";

  /**
   * An identifier that does nothing, i.e., cannot identify texts.
   */
  static LanguageIdentifier NO_IDENTIFIER = text -> UNKNOWN_LANGUAGE;

}
