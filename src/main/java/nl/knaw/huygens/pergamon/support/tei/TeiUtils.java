package nl.knaw.huygens.pergamon.support.tei;

import java.util.Optional;

import nl.knaw.huygens.tei.Element;

/**
 * Utility methods for handling TEI.
 */
public class TeiUtils {

  /**
   * Sets the @xml:id of the specified element to a normalized value.
   */
  public static void setNormalizedXmlIdAttribute(Element element, int number) {
    element.setAttribute(Attributes.ATTR_XML_ID, String.format("%s-%d", element.getName(), number));
  }

  public static String getIdAttribute(Element element) {
    String value = element.getAttribute(Attributes.ATTR_XML_ID);
    return value.isEmpty() ? element.getAttribute(Attributes.ATTR_ID) : value;
  }

  public static void setIdAttribute(Element element, String value) {
    element.setAttribute(Attributes.ATTR_XML_ID, value);
    element.removeAttribute(Attributes.ATTR_ID);
  }

  public static void copyIdAttribute(Element source, Element target) {
    setIdAttribute(target, getIdAttribute(source));
  }

  public static void removeIdAttribute(Element element) {
    element.removeAttribute(Attributes.ATTR_XML_ID);
    element.removeAttribute(Attributes.ATTR_ID);
  }

  // ---------------------------------------------------------------------------

  public static String getResponsibility(Element element) {
    return element.getAttribute(Attributes.RESP);
  }

  public static void setResponsibility(Element element, String value) {
    element.setAttribute(Attributes.RESP, value.trim());
  }

  public static void setResponsibility(Element element, Optional<String> name) {
    if (name.isPresent()) {
      setResponsibility(element, nameRef(name.get()));
    }
  }

  public static void appendResponsibility(Element element, Optional<String> annotator) {
    if (annotator.isPresent()) {
      setResponsibility(element, getResponsibility(element) + " " + nameRef(annotator.get()));
    }
  }

  private static String nameRef(String name) {
    return name.startsWith("#") ? name : "#" + name;
  }

  // ---------------------------------------------------------------------------

  /**
   * Returns the nearest ancestor with the specified name,
   * or {@code null} if no such element exists.
   */
  public static Element getAncestor(Element element, String name) {
    while (element != null) {
      if (element.hasName(name)) {
        return element;
      }
      element = element.getParent();
    }
    return null;
  }

  /**
   * Returns {@code true} if the specified element has an ancestor
   * with the specified name, {@code false} otherwise.
   */
  public static boolean hasAncestor(Element element, String name) {
    return getAncestor(element, name) != null;
  }

  /**
   * Returns the value of the attribute with the specified key if present,
   * otherwise the corresponding value for the parent element, recursively.
   */
  public static String getImpliedAttribute(Element element, String key, String defaultValue) {
    while (element != null) {
      String language = element.getAttribute(key);
      if (!language.isEmpty() && !language.equals("?")) {
        return language;
      }
      element = element.getParent();
    }
    return defaultValue;
  }

  public static String getImpliedLanguage(Element element) {
    return getImpliedAttribute(element, Element.LANGUAGE, "?");
  }

  public static String openTag(String tagName, String key, String value) {
    StringBuilder builder = new StringBuilder();
    new Element(tagName).withAttribute(key, value).appendOpenTagTo(builder);
    return builder.toString();
  }

  public static String closeTag(String tagName) {
    StringBuilder builder = new StringBuilder();
    new Element(tagName).appendCloseTagTo(builder);
    return builder.toString();
  }

  private TeiUtils() {
    throw new AssertionError("Non-instantiable class");
  }

}
