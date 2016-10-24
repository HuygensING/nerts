package nl.knaw.huygens.pergamon.nerts.matcher;

import static org.junit.Assert.assertEquals;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.common.collect.Sets;

public class REDictionaryMatcherTest {

  private MatcherFactory factory;

  @Before
  public void setup() {
    factory = new MatcherFactory(MatcherType.REG_EXP_MATCHER);
  }

  private void testMatches(int n, String expected, String text, String... names) {
    List<Match> matches = factory.getMatcher(Sets.newHashSet(names)) //
        .match(text);
    assertEquals(n, matches.size());
    matches.forEach(match -> assertEquals(expected, text.subSequence(match.getStart(), match.getEnd())));
  }

  @Test
  public void testSingleMatch() {
    testMatches(1, "simple", "A simple text", "complex", "simple");
  }

  @Test
  public void testMultipleMatches() {
    testMatches(2, "simple", "A simple, simple text", "complex", "simple");
  }

  @Test
  public void testWholeWordMatch() {
    testMatches(1, "abc", "ab abc abcd", "abc");
  }

  @Test
  public void testOverlappingMatch() {
    testMatches(1, "l'Isle", "Notre ami de l'Isle de Re.", "l'Isle", "Isle de Re");
  }

  @Test
  public void testCaseSensitiveMatch() {
    testMatches(1, "l'Isle", "Notre ami de l'Isle de Re.", "l'Isle", "isle de Re");
  }

}
