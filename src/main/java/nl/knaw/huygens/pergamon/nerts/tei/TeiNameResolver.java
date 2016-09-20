package nl.knaw.huygens.pergamon.nerts.tei;

import nl.knaw.huygens.pergamon.nerts.Ident;
import nl.knaw.huygens.pergamon.support.tei.Attributes;
import nl.knaw.huygens.pergamon.support.tei.TeiUtils;
import nl.knaw.huygens.tei.DelegatingVisitor;
import nl.knaw.huygens.tei.Element;
import nl.knaw.huygens.tei.Traversal;
import nl.knaw.huygens.tei.XmlContext;
import nl.knaw.huygens.tei.handlers.DefaultElementHandler;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class TeiNameResolver extends DelegatingVisitor<XmlContext> {

  private static Pattern WHITESPACE = Pattern.compile("\\s+");

  private final Predicate<Ident> identFilter;
  private final Optional<String> annotator;

  /**
   * Creates a new {@code TeiNameResolver} instance.
   *
   * @param identFilter  predicate for selecting identifications.
   * @param annotator    the annotator name; if present used as value of the {@code resp} attribute.
   * @param elementNames the names of the elements to be annotated.
   */
  public TeiNameResolver(Predicate<Ident> identFilter, Optional<String> annotator, String... elementNames) {
    super(new XmlContext());
    addElementHandler(new NameElementHandler(), elementNames);
    this.identFilter = identFilter;
    this.annotator = annotator;
  }

  public TeiNameResolver(Optional<String> annotator, String... elementNames) {
    this(ident -> true, annotator, elementNames);
  }

  private class NameElementHandler extends DefaultElementHandler<XmlContext> {
    @Override
    public Traversal leaveElement(Element element, XmlContext context) {
      if (element.hasAttribute(Attributes.KEYS)) {
        List<Ident> idents = fromAttribute(element.getAttribute(Attributes.KEYS));
        String type = resolveType(idents);
        if (!type.isEmpty()) {
          element.setName(type);
          TeiUtils.setResponsibility(element, annotator);
        }
        if (idents.size() == 1) {
          element.setAttribute(Attributes.KEY, idents.get(0).getId());
          element.removeAttribute(Attributes.KEYS);
          TeiUtils.setResponsibility(element, annotator);
        } else if (idents.size() > 1) {
          String value = idents.stream().map(Ident::toString).collect(Collectors.joining(" "));
          if (type.isEmpty()) {
            element.setAttribute(Attributes.KEYS, value);
          } else {
            // Remove type shared by all items
            element.setAttribute(Attributes.KEYS, value.replaceAll("\\b" + type + "\\|", ""));
          }
        }
      }
      return Traversal.NEXT;
    }

    private List<Ident> fromAttribute(String value) {
      return WHITESPACE.splitAsStream(value) //
          .map(Ident::new) //
          .filter(identFilter) //
          .collect(Collectors.toList());
    }

    private String resolveType(List<Ident> idents) {
      Set<String> types = idents.stream() //
          .map(Ident::getType) //
          .collect(Collectors.toSet());
      return (types.size() == 1) ? types.iterator().next() : "";
    }
  }

};
