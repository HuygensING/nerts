package nl.knaw.huygens.pergamon.nerts.text;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NameLinkFilterTest extends FilterTest {

  @Before
  public void setupFilter() {
    filter = new NameLinkFilter();
  }

  @Test
  public void testNoWhitespace() {
    Assert.assertEquals("de Denen van Vandaag komen niet in l'isle.", filter.apply("De Denen Van Vandaag komen niet in L'isle."));
  }

  @Test
  public void testFilterPreservesLength() {
    doTestLength(filter);
  }

}
