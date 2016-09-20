package nl.knaw.huygens.pergamon.support.tei.export;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

import nl.knaw.huygens.tei.Element;

public class AttributeFilterTest {

  private AttributeFilter createFilter(boolean keepOthers) {
    return new AttributeFilter(keepOthers, "a", "b", "c");
  }

  private Element createElement(String name, String... keys) {
    Element element = new Element(name);
    for (String key : keys) {
      element.setAttribute(key, "v");
    }
    return element;
  }

  private void process(AttributeFilter filter, Element element, String expected) {
    StringBuilder builder = new StringBuilder();
    Element filtered = filter.apply(element);
    filtered.appendOpenTagTo(builder);
    assertEquals(expected.replace('\'', '\"'), builder.toString());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testIdNotAllowed() {
    new AttributeFilter(true, "id");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testXmlIdNotAllowed() {
    new AttributeFilter(true, "xml:id");
  }

  @Test
  public void testElementWithNoAttributes() {
    AttributeFilter filter = createFilter(true);
    Element element = new Element("test");
    process(filter, element, "<test>");
  }

  @Test
  public void testDiscardOthers() {
    AttributeFilter filter = createFilter(false);
    Element element = createElement("test", "xml:id", "id", "b", "a", "x");
    process(filter, element, "<test xml:id='v' id='v' a='v' b='v'>");
  }

  @Test
  public void testKeepOthers() {
    AttributeFilter filter = createFilter(true);
    Element element = createElement("test", "a", "b", "x", "id");
    process(filter, element, "<test id='v' a='v' b='v' x='v'>");
  }

}
