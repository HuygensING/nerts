package nl.knaw.huygens.pergamon.support.tei.export;

import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.tei.Element;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.Function;

/**
 * Orders the attributes of a TEI element.
 */
public class AttributeFilter implements Function<Element, Element> {

  private final boolean keepOthers;
  private final List<String> keysInOrder;

  public AttributeFilter(boolean keepOthers, String... keys) {
    this.keepOthers = keepOthers;
    keysInOrder = new ArrayList<>();
    for (String key : keys) {
      if (key.equals(Attributes.ATTR_ID) || key.equals(Attributes.ATTR_XML_ID)) {
        throw new IllegalArgumentException("Invalid key: " + key);
      }
      keysInOrder.add(key);
    }
  }

  public AttributeFilter(String... keysInOrder) {
    this(true, keysInOrder);
  }

  @Override
  public Element apply(Element element) {
    if (keysInOrder.isEmpty()) {
      return element;
    } else {
      Element ordered = new Element(element.getName(), new LinkedHashMap<>());
      ordered.copyAttributeFrom(element, Attributes.ATTR_XML_ID);
      ordered.copyAttributeFrom(element, Attributes.ATTR_ID);
      for (String key : keysInOrder) {
        ordered.copyAttributeFrom(element, key);
      }
      if (keepOthers) {
        for (String key : element.getAttributeNames()) {
          if (!ordered.hasAttribute(key)) {
            ordered.copyAttributeFrom(element, key);
          }
        }
      }
      return ordered;
    }
  }

}
