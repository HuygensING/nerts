package nl.knaw.huygens.pergamon.support.tei.export;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.function.UnaryOperator;

import com.google.common.collect.Lists;

import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.tei.Element;

/**
 * Orders the attributes of a TEI element.
 */
public class AttributeFilter implements UnaryOperator<Element> {

  private final boolean keepOthers;
  private final List<String> keysInOrder;

  public AttributeFilter(boolean keepOthers, String... keys) {
    this.keepOthers = keepOthers;
    keysInOrder = Lists.newArrayList(Attributes.ATTR_XML_ID, Attributes.ATTR_ID, Attributes.ATTR_XML_LANG);
    for (String key : keys) {
      if (keysInOrder.contains(key)) {
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
    Element ordered = new Element(element.getName(), new LinkedHashMap<>());
    for (String key : keysInOrder) {
      // Copy attribute if it exists
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
