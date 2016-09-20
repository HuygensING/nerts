package nl.knaw.huygens.pergamon.nerts.text;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class NERTextNormalizerTest extends FilterTest {

  @Before
  public void setup() {
    filter = new NERTextNormalizer();
  }

  @Test
  public void testSpellingVariation() {
    Assert.assertEquals("Heinsii", filter.apply("Heinsij"));
    Assert.assertEquals("IIsbrands", filter.apply("IJsbrands"));
    Assert.assertEquals("Carcaui", filter.apply("Carcaui"));
    Assert.assertEquals("Carcaui", filter.apply("Carcavi"));
  }

  @Test
  public void testPunctuation() {
    Assert.assertEquals("Ioh. Uallis", filter.apply("Joh: Wallis"));
    Assert.assertEquals("M. Nassau Siegen", filter.apply("M. Nassau-Siegen"));
  }

  @Test
  public void testFilterPreservesLength() {
    doTestLength(filter);
  }

}
