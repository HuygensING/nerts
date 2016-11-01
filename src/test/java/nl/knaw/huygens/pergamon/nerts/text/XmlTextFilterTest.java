package nl.knaw.huygens.pergamon.nerts.text;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class XmlTextFilterTest extends FilterTest {

  @Before
  public void setupFilter() {
    filter = new XmlTextFilter();
  }

  @Test
  public void testCharacters() {
    Assert.assertEquals("Text containing &lt;, &gt;, and &amp;.", filter.apply("Text containing <, >, and &."));
  }

}
