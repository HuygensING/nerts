package nl.knaw.huygens.pergamon.nerts.matcher;

public enum MatcherType {
  // Resolves overlapping matches by choosing longest match
  AHO_CORASICK_MATCHER, //
  // Resolves overlapping matches by choosing leftmost match
  REG_EXP_MATCHER;
}
