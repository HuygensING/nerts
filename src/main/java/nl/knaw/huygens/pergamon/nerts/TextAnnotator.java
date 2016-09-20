package nl.knaw.huygens.pergamon.nerts;

import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;

/**
 * Add 'tags' around text fragments indicated by matches.
 * Fragments defined as exceptions will not be annotated.
 */
public class TextAnnotator {

  public static final String BEGIN_OF_NAME = "«BON»";
  public static final String END_OF_NAME = "«EON»";

  private static final String ANY_LANGUAGE = "!";

  private Multimap<String, String> exceptions;

  public TextAnnotator() {
    exceptions = ArrayListMultimap.create();
  }

  /**
   * Add exceptions for the specified language.
   */
  public TextAnnotator withSpecialExceptions(String lang, String... words) {
    for (String word : words) {
      exceptions.put(lang, word);
    }
    return this;
  }

  /**
   * Adds exceptions for any language.
   */
  public TextAnnotator withGeneralExceptions(String... words) {
    return withSpecialExceptions(ANY_LANGUAGE, words);
  }

  private boolean isException(String lang, String word) {
    return exceptions.containsEntry(ANY_LANGUAGE, word) || exceptions.containsEntry(lang, word);
  }

  public String annotate(String text, List<Match> matches, String lang) {
    StringBuilder builder = new StringBuilder();
    int pos = 0;
    for (Match match : matches) {
      int start = match.getStart();
      int end = match.getEnd();
      if (start < 0 || end > text.length()) {
        throw new IllegalStateException(String.format("Illegal index(es) %d %d in: %s", start, end, text));
      }
      if (start > pos) {
        builder.append(text, pos, start);
      }
      String word = text.substring(start, end);
      if (isException(lang, word)) {
        builder.append(word);
      } else {
        builder.append(BEGIN_OF_NAME).append(word).append(END_OF_NAME);
      }
      pos = end;
    }
    if (pos < text.length()) {
      builder.append(text, pos, text.length());
    }
    return builder.toString();
  }

}
