package nl.knaw.huygens.pergamon.nerts.matcher;

import java.util.Set;

public class MatcherFactory {

  private final MatcherType type;

  public MatcherFactory(MatcherType type) {
    this.type = type;
  }

  public MatcherFactory() {
    this(MatcherType.AHO_CORASICK_MATCHER);
  }

  public Matcher getMatcher(Set<String> names) {
    switch (type) {
    case AHO_CORASICK_MATCHER:
      return new ACDictionaryMatcher(names);
    case REG_EXP_MATCHER:
      return new REDictionaryMatcher(names);
    default:
      throw new IllegalArgumentException("Invalid matcher type");
    }
  }

}
