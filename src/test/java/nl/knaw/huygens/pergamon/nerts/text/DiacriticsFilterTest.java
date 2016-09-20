package nl.knaw.huygens.pergamon.nerts.text;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class DiacriticsFilterTest extends FilterTest {

  @Before
  public void setupFilter() {
    filter = new DiacriticsFilter();
  }

  @Test
  public void testMappedCharacters() {
    Assert.assertEquals("Foesius", filter.apply("Foësius"));
    Assert.assertEquals("Prepetit", filter.apply("Prépetit"));
    Assert.assertEquals("Lievre", filter.apply("Lièvre"));
    Assert.assertEquals("Abelard", filter.apply("Abélard"));
    Assert.assertEquals("Wurttemberg", filter.apply("Württemberg"));
    Assert.assertEquals("Savoye", filter.apply("Savoÿe"));
    Assert.assertEquals("Noailles", filter.apply("Nöailles"));
    Assert.assertEquals("d'Alencon", filter.apply("d'Alençon"));
    Assert.assertEquals("Penaranda", filter.apply("Peñaranda"));
    Assert.assertEquals("l'aine", filter.apply("l'aîné"));
  }

  @Test
  public void testIgnoredCharacters() {
    // Mapping ß --> ss would not preserve length
    Assert.assertEquals("Mißverstandnuße", filter.apply("Mißverständnüße"));
  }

  @Test
  public void testFilterPreservesLength() {
    doTestLength(filter);
  }

}
