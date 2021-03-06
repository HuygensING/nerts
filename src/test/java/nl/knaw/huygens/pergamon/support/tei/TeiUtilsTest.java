package nl.knaw.huygens.pergamon.support.tei;

import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;

import nl.knaw.huygens.tei.Element;

public class TeiUtilsTest {

  @Test
  public void testNormalizedXmlIdAttributeForLowerCase() {
    Element element = new Element("name");
    TeiUtils.setNormalizedXmlIdAttribute(element, 42);
    Assert.assertEquals("name-42", element.getAttribute(Attributes.ATTR_XML_ID));
    Assert.assertEquals("", element.getAttribute(Attributes.ATTR_ID));
  }

  @Test
  public void testNormalizedXmlIdAttributeForMixedCase() {
    Element element = new Element("miXed");
    TeiUtils.setNormalizedXmlIdAttribute(element, 3);
    Assert.assertEquals("miXed-3", element.getAttribute(Attributes.ATTR_XML_ID));
    Assert.assertEquals("", element.getAttribute(Attributes.ATTR_ID));
  }

  @Test
  public void testSetResponsibilityWithValue() {
    Element element = new Element("name");
    TeiUtils.setResponsibility(element, "ed1");
    Assert.assertEquals("ed1", TeiUtils.getResponsibility(element));
    TeiUtils.setResponsibility(element, " ed2 ");
    Assert.assertEquals("ed2", TeiUtils.getResponsibility(element));
  }

  @Test
  public void testSetResponsibilityWithAnnotator() {
    Element element = new Element("name");
    TeiUtils.setResponsibility(element, Optional.empty());
    Assert.assertEquals("", TeiUtils.getResponsibility(element));
    TeiUtils.setResponsibility(element, Optional.of("#ed1"));
    Assert.assertEquals("#ed1", TeiUtils.getResponsibility(element));
    TeiUtils.setResponsibility(element, Optional.of("ed2"));
    Assert.assertEquals("#ed2", TeiUtils.getResponsibility(element));
  }

  @Test
  public void testEnsureResponsibility() {
    Element element = new Element("name");
    TeiUtils.ensureResponsibility(element, "ed1");
    Assert.assertEquals("#ed1", TeiUtils.getResponsibility(element));
    TeiUtils.ensureResponsibility(element, "ed2");
    Assert.assertEquals("#ed1", TeiUtils.getResponsibility(element));
  }

  @Test
  public void testAppendResponsibilityWithAnnotator() {
    Element element = new Element("name");
    TeiUtils.appendResponsibility(element, Optional.empty());
    Assert.assertEquals("", TeiUtils.getResponsibility(element));
    TeiUtils.appendResponsibility(element, Optional.of("ed1"));
    Assert.assertEquals("#ed1", TeiUtils.getResponsibility(element));
    TeiUtils.appendResponsibility(element, Optional.of("#ed2"));
    Assert.assertEquals("#ed1 #ed2", TeiUtils.getResponsibility(element));
  }

  @Test(expected = NullPointerException.class)
  public void testHasAncestorWithNullElement() {
    TeiUtils.hasAncestor(null, "name");
  }

  @Test
  public void testHasAncestor() {
    Element element = new Element("name");
    Assert.assertFalse(TeiUtils.hasAncestor(element, "name"));
    element.setParent(new Element("name"));
    Assert.assertTrue(TeiUtils.hasAncestor(element, "name"));
  }

}
